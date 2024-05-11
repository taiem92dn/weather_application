package com.taingdev.weatherapp.data.repositories

import com.taingdev.weatherapp.data.local.LocalFavoriteCityDataSource
import com.taingdev.weatherapp.domain.data.CityRepository
import com.taingdev.weatherapp.domain.entity.City
import com.taingdev.weatherapp.data.remote.RemoteCityDataSource

class CityRepositoryImpl(
    private val remoteCityDataSource: RemoteCityDataSource,
    private val localFavoriteCityDataSource: LocalFavoriteCityDataSource
): CityRepository {
    override suspend fun searchCities(query: String): List<City> {
        return remoteCityDataSource.searchCities(query)
    }

    override suspend fun getFavoriteCities(): List<City> {
        return localFavoriteCityDataSource.getFavoriteCities()
    }

    override suspend fun saveFavoriteCity(city: City) {
        localFavoriteCityDataSource.saveFavoriteCity(city)
    }

    override suspend fun removeFavoriteCity(city: City) {
        localFavoriteCityDataSource.removeFavoriteCity(city)
    }

    override suspend fun checkFavoriteCity(city: City): Boolean {
        return localFavoriteCityDataSource.checkFavoriteCity(city)
    }
}