package com.taingdev.weatherapp.domain.usecase

import com.taingdev.weatherapp.domain.data.CityRepository
import com.taingdev.weatherapp.domain.entity.City

class RemoveFavoriteCityUseCase (
    private val cityRepository: CityRepository
){
    suspend fun removeFavoriteCity(city: City) {
        cityRepository.removeFavoriteCity(city)
    }
}