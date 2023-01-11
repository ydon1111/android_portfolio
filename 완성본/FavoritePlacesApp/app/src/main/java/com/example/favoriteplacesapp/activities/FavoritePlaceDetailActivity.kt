package com.example.favoriteplacesapp.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.favoriteplacesapp.R

import com.example.favoriteplacesapp.databinding.ActivityFavoritePlaceDetailBinding
import com.example.favoriteplacesapp.databinding.ActivityMainBinding.bind

import com.example.favoriteplacesapp.databinding.ActivityMainBinding.inflate
import com.example.favoriteplacesapp.models.FavoritePlaceModel

class FavoritePlaceDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoritePlaceDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFavoritePlaceDetailBinding.inflate(layoutInflater)

        setContentView(binding.root)

        var favoritePlaceDetailModel: FavoritePlaceModel? = null

        if (intent.hasExtra(MainActivity.EXTRA_PLACE_DETAILS)) {
            favoritePlaceDetailModel = intent.getSerializableExtra(
                MainActivity.EXTRA_PLACE_DETAILS
            ) as FavoritePlaceModel
        }

        if(favoritePlaceDetailModel != null){
            setSupportActionBar(binding.toolbarFavoritePlaceDetail)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = favoritePlaceDetailModel.title

            binding.toolbarFavoritePlaceDetail.setNavigationOnClickListener {
                onBackPressed()
            }

            binding.ivPlaceImage.setImageURI(Uri.parse(favoritePlaceDetailModel.image))
            binding.tvDescription.text = favoritePlaceDetailModel.description
            binding.tvLocation.text =favoritePlaceDetailModel.location

            binding.btnViewOnMap.setOnClickListener{
                val intent = Intent(this, MapActivity::class.java)
                intent.putExtra(MainActivity.EXTRA_PLACE_DETAILS,favoritePlaceDetailModel)
                startActivity(intent)
            }

        }
    }


}