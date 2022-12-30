package com.example.mapsdemo.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.mapsdemo.databinding.ActivitySearchLocationBinding
import com.example.mapsdemo.localStorage.PlaceModel
import com.example.mapsdemo.utils.Constants
import com.example.mapsdemo.utils.FetchURL
import com.example.mapsdemo.utils.SortPlaces
import com.example.mapsdemo.utils.TaskLoadedCallback
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import java.util.*


class DirectionsActivity : BaseActivity(), OnMapReadyCallback, TaskLoadedCallback {

    private lateinit var mBinding: ActivitySearchLocationBinding
    private lateinit var mapFragment: SupportMapFragment
    private var googleMap: GoogleMap? = null
    private var markedPlaces: List<PlaceModel>? = null

    var starting_lat: Double? = null
    var starting_lng: Double? = null
    private var currentPolyline: Polyline? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivitySearchLocationBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        initDb()

        markedPlaces = database.placesDao().getAll()

        mBinding.llSearch.visibility = View.GONE
        mBinding.rvPlaces.visibility = View.GONE

        mapFragment =
            supportFragmentManager.findFragmentById(com.example.mapsdemo.R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)

        Places.initialize(this, Constants.MAPS_API_KEY)

        mBinding.ivBack.setOnClickListener {
            onBackPressed()
        }

    }

    private fun getUrl(place1: LatLng, place2: LatLng, directionMode: String): String? {
//        if (markers.size < 2) return ""
        // Origin of route
        val str_origin = "origin=" + place1.latitude + "," + place1.longitude
        // Destination of route
        val str_dest = "destination=" + place2.latitude + "," + place2.longitude
        // Mode
        val mode = "mode=$directionMode"
        /*val parameters = if (markers.size > 2) {
            //Waypoints
            var waypoints = "waypoints="
            for (i in 1..markers.size - 2) {
                waypoints += markers[i].id + ","
            }
            waypoints = waypoints.substring(0, waypoints.length - 1)
            "$str_origin&$str_dest&$mode$waypoints"
        } else {
            // Building the parameters to the web service
            "$str_origin&$str_dest&$mode"
        }*/
        val parameters = "$str_origin&$str_dest&$mode"
        // Output format
        val output = "json"
        // Building the url to the web service
        return "https://maps.googleapis.com/maps/api/directions/$output?$parameters&key=" + Constants.MAPS_API_KEY
    }

    private fun getDirectionsUrl(
        markerPoints: List<PlaceModel>
    ): String? {
        val str_origin =
            "origin=" + markedPlaces?.first()?.latitude + "," + markedPlaces?.first()?.longitude
        val str_dest =
            "destination=" + markedPlaces?.last()?.latitude + "," + markedPlaces?.last()?.longitude
        val sensor = "sensor=false"
        var waypoints = ""
        for (i in 1 until markerPoints.size - 1) {
            val point = LatLng(markerPoints[i].latitude, markerPoints[i].longitude)
            if (i == 1) waypoints = "waypoints="
            waypoints += point.latitude.toString() + "," + point.longitude + "|"
        }
        val parameters = "$str_origin&$str_dest&rankBy=distance&$sensor&$waypoints"
        val output = "json"
        return "https://maps.googleapis.com/maps/api/directions/$output?$parameters&key=" + Constants.MAPS_API_KEY
    }

    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0
        Log.d("mylog", "Added Markers")

        for (p in markedPlaces!!) {
            Log.i("Places before sorting", "Place: " + p.latitude)
        }

        Collections.sort(
            markedPlaces!!,
            SortPlaces(LatLng(markedPlaces?.first()!!.latitude, markedPlaces?.first()!!.longitude))
        )

        for (p in markedPlaces!!) {
            Log.i("Places after sorting", "Place: " + p.latitude)
        }

        googleMap?.animateCamera(
            CameraUpdateFactory.newCameraPosition(
                CameraPosition.Builder().target(
                    LatLng(markedPlaces?.first()!!.latitude, markedPlaces?.first()!!.longitude)
                ).zoom(12f).build()
            )
        )
        for (i in 0 until markedPlaces!!.size - 1)
            drawRoute(markedPlaces!![i], markedPlaces!![i + 1])
    }

    private fun drawRoute(origin: PlaceModel, dest: PlaceModel) {
        val place1 = MarkerOptions().position(
            LatLng(
                origin.latitude,
                origin.longitude
            )
        ).title("Location 1")
        val place2 = MarkerOptions().position(
            LatLng(
                dest.latitude,
                dest.longitude
            )
        ).title("Location 2")
        googleMap?.addMarker(place1)
        googleMap?.addMarker(place2)
        FetchURL(this).execute(getDirectionsUrl(markedPlaces!!), "driving")
    }

    override fun onTaskDone(vararg values: Any?) {
        if (currentPolyline != null) currentPolyline?.remove()
        currentPolyline = (values[0] as PolylineOptions?)?.let { googleMap?.addPolyline(it) }
    }

}