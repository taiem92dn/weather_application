package com.taingdev.weatherapp.model


data class ForecastItem (
    val city: CityItem,
    val data: List<ForecastDatumItem>
)

data class ForecastDatumItem(
    val date: Long,
    val temperature: Double,
    val feelsLike: Double,
    val humidity: Int,
    val windSpeed: Double,
    val windDegree: Int,
    val weather: List<String>
)
