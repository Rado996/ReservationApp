package com.bachelor.reservation.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bachelor.reservationapp.R
import com.google.android.gms.maps.*
import kotlinx.android.synthetic.main.fragment_location.view.*
import android.view.LayoutInflater as LayoutInflater

class LocationFragment : Fragment() {
    //,OnMapReadyCallback
    private lateinit var viewOfLayout: View
    private var mMap: MapView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewOfLayout = inflater?.inflate(R.layout.fragment_location, container, false)

//        mMap = viewOfLayout?.findViewById(R.id.mapView) as MapView
//        mMap?.onCreate(savedInstanceState)
//        mMap?.getMapAsync(this)
        viewOfLayout.webView.settings.javaScriptEnabled = true
        viewOfLayout.webView.loadUrl("https://www.google.com/maps/place/Mirage/@49.2244495,18.7404273,20.13z/data=!4m5!3m4!1s0x0:0xaddf4af3b1a0d2a3!8m2!3d49.2244223!4d18.7404872")

        return viewOfLayout
    }

    override fun onResume() {
        super.onResume()
        mMap?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mMap?.onPause()
    }

    override fun onStart() {
        super.onStart()
        mMap?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mMap?.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMap?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMap?.onLowMemory()
    }

//    override fun onMapReady(googleMap: GoogleMap) {
//        val target = LatLng(49.224429768132765, 18.740485671425663)
//        val zoom: Float = 11.0F
//        googleMap.addMarker(MarkerOptions().position(target).title("Marker"))
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(target))
//        googleMap.setMinZoomPreference(zoom)
//
//    }

}