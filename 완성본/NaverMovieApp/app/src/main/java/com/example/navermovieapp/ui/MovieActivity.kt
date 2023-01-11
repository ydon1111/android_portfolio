package com.example.navermovieapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.ExperimentalPagingApi
import com.example.navermovieapp.R
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
@ExperimentalPagingApi
class MovieActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

    }
}

