package com.taingdev.weatherapp.domain.usecase

import com.taingdev.weatherapp.domain.data.CityRepository
import com.taingdev.weatherapp.domain.entity.City

class GetFavoriteCitiesUseCase(
    private val cityRepository: CityRepository
) {
    suspend fun getFavoriteCities(): List<City> {
        return cityRepository.getFavoriteCities()
    }
}