package com.taingdev.weatherapp.domain.usecase

import com.taingdev.weatherapp.domain.data.CityRepository
import com.taingdev.weatherapp.domain.entity.City

class SearchCitiesUseCase(
    private val cityRepository: CityRepository
) {
    suspend fun searchCities(query: String): List<City> {
        return cityRepository.searchCities(query)
    }
}