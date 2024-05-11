package com.taingdev.weatherapp.data.repositories

import com.taingdev.weatherapp.data.remote.RemoteForecastDataSource
import com.taingdev.weatherapp.domain.data.ForecastRepository
import com.taingdev.weatherapp.domain.entity.Forecast

class ForecastRepositoryImpl(
    private val remoteForecastDataSource: RemoteForecastDataSource
): ForecastRepository {
    override suspend fun fetchForecast(lat: Double, lon: Double): Forecast {
        return remoteForecastDataSource.fetchForecast(lat, lon)
    }
}