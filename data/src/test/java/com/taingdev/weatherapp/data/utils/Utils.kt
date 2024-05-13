package com.taingdev.weatherapp.data.utils

import com.taingdev.weatherapp.domain.entity.City
import com.taingdev.weatherapp.domain.entity.Forecast
import com.taingdev.weatherapp.domain.entity.ForecastDatum

const val LAT = 1.0

const val LON = 2.0
fun provideCity() = City("", LAT, LON, "")
fun provideCityList() = listOf(provideCity())

fun provideForecast() = Forecast(
    provideCity(),
    listOf(provideForecastDatum())
)

private const val date = 123L

fun provideForecastDatum() =
    ForecastDatum(
        date = date,
        temperature = 2.0,
        feelsLike = 3.0,
        humidity = 4,
        windSpeed = 7.0,
        windDegree = 4,
        listOf("")
    )
