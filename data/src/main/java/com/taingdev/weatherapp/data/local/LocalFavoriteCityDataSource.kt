package com.taingdev.weatherapp.data.local

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.taingdev.weatherapp.domain.data.FavoriteCityDataSource
import com.taingdev.weatherapp.domain.entity.City
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

private const val FAVORITE_CITIES = "favorite_cities"

class LocalFavoriteCityDataSource @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    @Named("ioDispatcher")
    private val ioDispatcher: CoroutineDispatcher,
) : FavoriteCityDataSource {

    private val listType = object : TypeToken<ArrayList<City>>() {}.type
    private val gson = Gson()

    override suspend fun getFavoriteCities(): List<City> {
        sharedPreferences.getString(FAVORITE_CITIES, null)?.let { jsonSource ->
            return withContext(ioDispatcher) {
                gson.fromJson(jsonSource, listType)
            }
        }
            ?: return emptyList()
    }

    override suspend fun saveFavoriteCity(city: City) {
        val list = getFavoriteCities().let {
            if (it.isEmpty())
                listOf(city)
            else {
                (it as MutableList).add(city)
                it
            }
        }

        sharedPreferences.edit()
            .putString(FAVORITE_CITIES, withContext(ioDispatcher) { gson.toJson(list, listType) })
            .apply()
    }

    override suspend fun removeFavoriteCity(city: City) {
        val list = getFavoriteCities().let {
            if (it.isEmpty())
                emptyList<City>()
            else {
                (it as MutableList).remove(city)
                it
            }
        }

        sharedPreferences.edit()
            .putString(FAVORITE_CITIES, withContext(ioDispatcher) { gson.toJson(list, listType) })
            .apply()
    }

    override suspend fun checkFavoriteCity(city: City): Boolean {
        return withContext(ioDispatcher) {
            getFavoriteCities().contains(city)
        }
    }
}