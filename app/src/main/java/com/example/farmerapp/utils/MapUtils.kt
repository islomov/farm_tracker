package com.example.farmerapp.utils

import com.google.android.gms.maps.model.LatLng

object MapUtils {



    fun getCenterOfPolygon(latLngList: List<LatLng>): LatLng? {
        val centroid = doubleArrayOf(0.0, 0.0)
        for (i in latLngList.indices) {
            centroid[0] += latLngList[i].latitude
            centroid[1] += latLngList[i].longitude
        }
        val totalPoints = latLngList.size
        return LatLng(centroid[0] / totalPoints, centroid[1] / totalPoints)
    }

}