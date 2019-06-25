package com.sovize.ultracop.views.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.sovize.ultracop.R
import com.sovize.ultracop.utilities.AppKey

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var lat = 0.toDouble()
    private var lon = 0.toDouble()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        lat = intent.getDoubleExtra(AppKey.latitude, 32.055)
        lon = intent.getDoubleExtra(AppKey.longitude, -89.054)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     * 1: World
     *5: Landmass/continent
     *10: City
     *15: Streets
     *20: Buildings
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val place = LatLng(lat, lon)
        mMap.addMarker(MarkerOptions().position(place).title(getString(R.string.locationS)))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place, 20f))
    }
}
