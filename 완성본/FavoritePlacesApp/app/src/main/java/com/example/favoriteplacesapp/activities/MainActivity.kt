package com.example.favoriteplacesapp.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.favoriteplacesapp.adapters.FavoritePlacesAdapter
import com.example.favoriteplacesapp.database.DatabaseHandler
import com.example.favoriteplacesapp.databinding.ActivityMainBinding
import com.example.favoriteplacesapp.models.FavoritePlaceModel
import com.example.favoriteplacesapp.utils.SwipeToDeleteCallback
import com.example.favoriteplacesapp.utils.SwipeToEditCallBack

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    companion object {
        private const val CAMERA_PERMISSION_CODE = 1
        private const val CAMERA_REQUEST_CODE = 2
        private const val ADD_PLACE_ACTIVITY_REQUEST_CODE = 1
        var EXTRA_PLACE_DETAILS = "extra_place_details"
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fabAddHappyPlace.setOnClickListener {
            val intent = Intent(this@MainActivity, AddHappyPlaceActivity::class.java)
            startActivityForResult(intent, ADD_PLACE_ACTIVITY_REQUEST_CODE)
        }

        getFavoritePlacesListFromLocalDB()
    }

    private fun getFavoritePlacesListFromLocalDB() {
        val dbHandler = DatabaseHandler(this)
        val getFavoritePlaceList: ArrayList<FavoritePlaceModel> = dbHandler.getFavoritePlacesList()

        if (getFavoritePlaceList.size > 0) {
            binding.rvFavoritePlacesList.visibility = View.VISIBLE
            binding.tvNoRecordsAvailable.visibility = View.GONE
            setupFavoritePlacesRecyclerView(getFavoritePlaceList)

        } else {
            binding.rvFavoritePlacesList.visibility = View.GONE
            binding.tvNoRecordsAvailable.visibility = View.VISIBLE
        }
    }

    private fun setupFavoritePlacesRecyclerView(
        favoritePlaceList: ArrayList<FavoritePlaceModel>
    ) {
        binding.rvFavoritePlacesList.layoutManager =
            LinearLayoutManager(this)
        binding.rvFavoritePlacesList.setHasFixedSize(true)

        val placesAdapter = FavoritePlacesAdapter(this, favoritePlaceList)
        binding.rvFavoritePlacesList.adapter = placesAdapter

        placesAdapter.setOnClickListener(object : FavoritePlacesAdapter.OnClickListener {
            override fun onClick(position: Int, model: FavoritePlaceModel) {
                val intent = Intent(
                    this@MainActivity,
                    FavoritePlaceDetailActivity::class.java
                )
                intent.putExtra(EXTRA_PLACE_DETAILS, model)
                startActivity(intent)
            }
        })

        val editSwipeHandler = object : SwipeToEditCallBack(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val adapter = binding.rvFavoritePlacesList.adapter as FavoritePlacesAdapter
                adapter.notifyEditItem(
                    this@MainActivity,
                    viewHolder.adapterPosition,
                    ADD_PLACE_ACTIVITY_REQUEST_CODE
                )
            }
        }
        val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
        editItemTouchHelper.attachToRecyclerView(binding.rvFavoritePlacesList)

        val deleteSwipeHandler = object : SwipeToDeleteCallback(this){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = binding.rvFavoritePlacesList.adapter as FavoritePlacesAdapter
                adapter.removeAt(viewHolder.adapterPosition)

                getFavoritePlacesListFromLocalDB()
            }
        }
        val deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
        deleteItemTouchHelper.attachToRecyclerView(binding.rvFavoritePlacesList)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_PLACE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                getFavoritePlacesListFromLocalDB()
            } else {
                Log.e("Activity", "취소 또는 돌아가기")
            }
        }
    }
}