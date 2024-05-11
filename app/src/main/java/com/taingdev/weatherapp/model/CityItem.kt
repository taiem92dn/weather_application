package com.taingdev.weatherapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CityItem (
    val name: String,
    val lat: Double,
    val lon: Double,
    val country: String
): Parcelable
