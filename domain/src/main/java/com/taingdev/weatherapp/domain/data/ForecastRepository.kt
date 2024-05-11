package com.taingdev.weatherapp.domain.data

import com.taingdev.weatherapp.domain.entity.Forecast

interface ForecastRepository {
    suspend fun fetchForecast(lat: Double, lon: Double): Forecast
}