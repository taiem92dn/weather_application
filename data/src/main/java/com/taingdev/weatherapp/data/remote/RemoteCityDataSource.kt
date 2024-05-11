package com.taingdev.weatherapp.data.remote

import com.taingdev.weatherapp.domain.data.CityDataSource
import com.taingdev.weatherapp.domain.entity.City
import com.taingdev.weatherapp.data.mapper.mapTo
import javax.inject.Inject

class RemoteCityDataSource @Inject constructor(
   private val weatherService: OpenWeatherMapService
): CityDataSource {
    override suspend fun searchCities(query: String): List<City> {
        return weatherService.searchCities(query, limit = 10).map {
            it.mapTo()
        }
    }

}