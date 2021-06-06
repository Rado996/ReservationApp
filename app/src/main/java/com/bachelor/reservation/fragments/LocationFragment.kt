package com.bachelor.reservation.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.bachelor.reservationapp.R
import com.google.android.gms.maps.*
import kotlinx.android.synthetic.main.fragment_location.view.*


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
//        mMap?.getMapAsync(OnMapReadyCallback {
//
//        })
        viewOfLayout.webView.settings.javaScriptEnabled = true
        viewOfLayout.webView.setWebViewClient(WebViewClient())
        viewOfLayout.webView.loadUrl("https://www.google.sk/maps/place/Koceľova+5836%2F4,+821+08+Bratislava/@48.1530253,17.125227,17z/data=!3m1!4b1!4m5!3m4!1s0x476c8949fc822f39:0x9ecf8cccd914515!8m2!3d48.1530253!4d17.1274157")

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