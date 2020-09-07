package com.example.farmerapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.farmerapp.utils.DataManager
import com.example.farmerapp.model.Farm
import com.example.farmerapp.R
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_farm_list.*

class FarmListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_farm_list)
        val farm1 = Farm(
            1,
            "Moo Valley Farm",
            R.drawable.placeholder,
            LatLng(35.8701082, -85.4082868)
        )
        val farm2 = Farm(
            2,
            "P K Lewis Farm",
            R.drawable.img1,
            LatLng(35.858123, -85.3655528)
        )
        val farm3 = Farm(
            3,
            "M La Fever Farm",
            R.drawable.img2,
            LatLng(35.8274619, -85.4513143)
        )
        val farm4 = Farm(
            4,
            "Pictsweet Farm",
            R.drawable.img3,
            LatLng(35.9278405, -85.3412127)
        )
        DataManager.map[farm1.id] = farm1
        DataManager.map[farm2.id] = farm2
        DataManager.map[farm3.id] = farm3
        DataManager.map[farm4.id] = farm4
        val list = arrayListOf<Farm>(farm1,farm2,farm3,farm4)
        val adapter = RecyclerAdapter(list) {
            val intent = Intent(this, MapsActivity::class.java)
            intent.putExtra("id", it)
            startActivity(intent)
        }
        recycler.adapter = adapter
    }
}