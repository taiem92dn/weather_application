package com.taingdev.weatherapp.domain.data

import com.taingdev.weatherapp.domain.entity.City

interface CityRepository {

    suspend fun searchCities(query: String): List<City>

    suspend fun getFavoriteCities(): List<City>

    suspend fun saveFavoriteCity(city: City)

    suspend fun removeFavoriteCity(city: City)

    suspend fun checkFavoriteCity(city: City): Boolean

}