package com.example.farmerapp.ui

import android.annotation.SuppressLint
import android.graphics.*
import android.graphics.Paint.Align
import android.os.Bundle
import android.view.MotionEvent
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.farmerapp.utils.DataManager
import com.example.farmerapp.model.Farm
import com.example.farmerapp.R
import com.example.farmerapp.utils.MapUtils
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_maps.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    lateinit var farm: Farm
    val currentLatLng = mutableListOf<LatLng>()
    var polyline: Polyline? = null
    var projection: Projection? = null
    var isMapDrawable = false
    var polygonId = 0;
    val markerMap: HashMap<Int, Marker> = HashMap()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)
        val id = intent.extras?.getInt("id")
        farm = DataManager.map[id]!!
        btn_movable.setOnClickListener {
            isMapDrawable = false
        }

        btn_draw_shape.setOnClickListener {
            drawMap()
        }

        btn_draw_polyline.setOnClickListener {
            isMapDrawable = true
        }

        fram_map.setOnTouchListener { v, event ->
            if (isMapDrawable) {
                val x = event.x
                val y = event.y

                val x_co = Math.round(x)
                val y_co = Math.round(y)

                projection = map.projection
                val x_y_points = Point(x_co, y_co)

                val latLng = map.projection.fromScreenLocation(x_y_points)

                val eventaction = event.action
                when (eventaction) {
                    MotionEvent.ACTION_DOWN -> {
                        currentLatLng.add(latLng)
                        drawPolyline()
                    }
                    MotionEvent.ACTION_MOVE -> {
                        currentLatLng.add(latLng)
                        drawPolyline()
                    }
                }
            }
            isMapDrawable
        }


    }

    private fun drawPolyline() {
        if (currentLatLng.isNotEmpty()) {
            val polyLineOptions = PolylineOptions()
            polyLineOptions.addAll(currentLatLng)
            polyLineOptions.color(Color.RED)
            polyLineOptions.width(3f)
            polyline = map.addPolyline(polyLineOptions)
        }
    }

    private fun drawMap() {
        if (currentLatLng.isNotEmpty()) {
            polyline?.remove()
            val newLatLng = mutableListOf<LatLng>()
            newLatLng.addAll(currentLatLng)
            val polygonOptions = PolygonOptions()
            polygonOptions.addAll(currentLatLng)
            polygonOptions.strokeColor(Color.TRANSPARENT)
            polygonOptions.fillColor(0x65000000)
            val polygon = map.addPolygon(polygonOptions)
            val centerOfPolygon = MapUtils.getCenterOfPolygon(newLatLng)
            polygon.tag = polygonId
            val marker = addTextForWateringLevel(centerOfPolygon!!, "0%")
            markerMap[polygonId] = marker
            polygon.isClickable = true
            currentLatLng.clear()
            farm.polygonsOptions.add(polygonOptions)
            farm.polygons.add(polygon)
            farm.centerOfPolygons[polygonId] = centerOfPolygon
            farm.wateringLevel[polygonId] = "0"
            farm.polygonIds.add(polygonId)
            polygonId++
            isMapDrawable = false
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.mapType = GoogleMap.MAP_TYPE_SATELLITE
        val markerOptions = MarkerOptions().position(farm.latLng)
        map.addMarker(markerOptions)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(farm.latLng, 15f))
        map.setOnPolygonClickListener { polygon ->
            val id = polygon.tag as Int
            showWateringLevelDialog(id)
        }
        for (polygonOptions in farm.polygonsOptions) {
            val polygon = map.addPolygon(polygonOptions)
            polygon.isClickable = true
        }
        for (id in farm.polygonIds) {
            farm.centerOfPolygons[id]?.let {centerOfPolygon->
                farm.wateringLevel[id]?.let {wateringLevel->
                    val marker = addTextForWateringLevel(centerOfPolygon, "$wateringLevel%")
                    markerMap[id] = marker
                }
            }

        }
    }

    private fun showWateringLevelDialog(id: Int) {
        val level = farm.wateringLevel[id]
        level?.let {
            val wateringDialogFragment =
                WateringDialogFragment(level) {
                    farm.wateringLevel[id] = it
                    val centerOfPolygon = farm.centerOfPolygons[id]
                    markerMap[id]?.remove()
                    val marker = addTextForWateringLevel(centerOfPolygon!!, "$it%")
                    markerMap[id] = marker
                }
            wateringDialogFragment.show(supportFragmentManager, "Dialog")
        }

    }

    private fun addTextForWateringLevel(location: LatLng, text: String): Marker {

        val textView = TextView(this)
        textView.text = text
        textView.textSize = 14.toFloat()
        val paintText: Paint = textView.paint
        val boundsText = Rect()
        paintText.getTextBounds(text, 0, textView.length(), boundsText)
        paintText.textAlign = Align.CENTER
        val conf = Bitmap.Config.ARGB_8888
        val bmpText = Bitmap.createBitmap(
            boundsText.width() + 2
                    * 10, boundsText.height() + 2 * 10, conf
        )
        val canvasText = Canvas(bmpText)
        paintText.color = Color.WHITE
        canvasText.drawText(
            text, canvasText.width / 2f,
            (canvasText.height * 1f) - 10 - boundsText.bottom, paintText
        )
        val markerOptions = MarkerOptions()
            .position(location)
            .icon(BitmapDescriptorFactory.fromBitmap(bmpText))
            .anchor(0.5f, 1f)
        return map.addMarker(markerOptions)!!
    }

}