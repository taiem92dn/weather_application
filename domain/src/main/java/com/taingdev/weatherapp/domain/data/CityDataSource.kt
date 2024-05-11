package com.taingdev.weatherapp.domain.data

import com.taingdev.weatherapp.domain.entity.City

interface CityDataSource {
    suspend fun searchCities(query: String): List<City>
}