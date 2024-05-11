package com.taingdev.weatherapp.domain.usecase

import com.taingdev.weatherapp.domain.data.CityRepository
import com.taingdev.weatherapp.domain.entity.City

class CheckFavoriteCityUseCase(
    private val cityRepository: CityRepository
) {
    suspend fun checkFavoriteCity(city: City): Boolean {
        return cityRepository.checkFavoriteCity(city)
    }
}