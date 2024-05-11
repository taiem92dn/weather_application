package com.taingdev.weatherapp.domain.entity

data class Forecast(
    val city: City,
    val data: List<ForecastDatum>
)
