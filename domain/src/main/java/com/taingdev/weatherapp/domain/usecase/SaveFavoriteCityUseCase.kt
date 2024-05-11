package com.taingdev.weatherapp.domain.usecase

import com.taingdev.weatherapp.domain.data.CityRepository
import com.taingdev.weatherapp.domain.entity.City

class SaveFavoriteCityUseCase(
    private val cityRepository: CityRepository
) {
    suspend fun saveFavoriteCity(city: City) {
        cityRepository.saveFavoriteCity(city)
    }
}