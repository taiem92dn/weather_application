package com.taingdev.weatherapp.domain.entity

import android.health.connect.datatypes.units.Temperature

data class ForecastDatum(
    val date: Long,
    val temperature: Double,
    val feelsLike: Double,
    val humidity: Int,
    val windSpeed: Double,
    val windDegree: Int,
    val weather: List<String>
)
