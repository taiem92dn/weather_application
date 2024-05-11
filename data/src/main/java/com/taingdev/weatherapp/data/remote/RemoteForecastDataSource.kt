package com.taingdev.weatherapp.data.remote

import com.taingdev.weatherapp.data.mapper.mapTo
import com.taingdev.weatherapp.domain.data.ForecastDataSource
import com.taingdev.weatherapp.domain.entity.Forecast
import javax.inject.Inject

class RemoteForecastDataSource @Inject constructor(
    private val weatherService: OpenWeatherMapService
) : ForecastDataSource {
    override suspend fun fetchForecast(lat: Double, lon: Double): Forecast {
        return weatherService.fetchForecast(lat, lon, count = 4, units = "metric").mapTo()
    }

}
