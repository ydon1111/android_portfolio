package com.example.favoriteplacesapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.favoriteplacesapp.R

import com.example.favoriteplacesapp.databinding.ActivityMapBinding
import com.example.favoriteplacesapp.models.FavoritePlaceModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityMapBinding

    private var mFavoritePlaceDetails: FavoritePlaceModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(MainActivity.EXTRA_PLACE_DETAILS)) {
            mFavoritePlaceDetails =
                intent.getSerializableExtra(MainActivity.EXTRA_PLACE_DETAILS)
                        as FavoritePlaceModel
        }

        if (mFavoritePlaceDetails != null) {
            setSupportActionBar(binding.toolbarMap)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = mFavoritePlaceDetails!!.title

            binding.toolbarMap.setNavigationOnClickListener {
                onBackPressed()
            }

            val supportMapFragment: SupportMapFragment =
                supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

            supportMapFragment.getMapAsync(this)

        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val position = LatLng(mFavoritePlaceDetails!!.latitude, mFavoritePlaceDetails!!.longitude)
        googleMap.addMarker(MarkerOptions().position(position).title(mFavoritePlaceDetails!!.location))
        val newLatLngZoom = CameraUpdateFactory.newLatLngZoom(position,15f)
        googleMap.animateCamera(newLatLngZoom)
    }
}