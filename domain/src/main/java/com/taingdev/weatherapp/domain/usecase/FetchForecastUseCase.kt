package com.taingdev.weatherapp.domain.usecase

import com.taingdev.weatherapp.domain.data.ForecastRepository
import com.taingdev.weatherapp.domain.entity.Forecast

class FetchForecastUseCase(
    private val forecastRepository: ForecastRepository
) {
    suspend fun fetchForecast(lat: Double, lon: Double): Forecast {
        return forecastRepository.fetchForecast(lat, lon)
    }
}
