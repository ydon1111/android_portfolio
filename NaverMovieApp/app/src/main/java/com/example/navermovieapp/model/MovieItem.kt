package com.example.navermovieapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class MovieItem(
    val title: String,
    val link: String,
    val image: String,
    val pubDate: String,
    val userRating: String,
) : Parcelable