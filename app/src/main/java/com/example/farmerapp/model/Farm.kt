package com.example.farmerapp.model

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polygon
import com.google.android.gms.maps.model.PolygonOptions
import com.google.android.gms.maps.model.Polyline

class Farm(val id:Int,val title:String,val img:Int,val latLng: LatLng) {
    val polygonsOptions:MutableList<PolygonOptions> = mutableListOf()
    val polygons:MutableList<Polygon> = mutableListOf()
    val polygonIds:MutableList<Int> = mutableListOf()
    val wateringLevel: HashMap<Int,Int> = HashMap()
    val centerOfPolygons: HashMap<Int,LatLng> = HashMap()
}