package com.taingdev.weatherapp.data.remote

import com.taingdev.weatherapp.data.model.CityQuery
import com.taingdev.weatherapp.data.model.ForecastResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherMapService {

    @GET("geo/1.0/direct")
    suspend fun searchCities(
        @Query("q") query: String,
        @Query("limit") limit: Int
    ): List<CityQuery>

    @GET("data/2.5/forecast/daily")
    suspend fun fetchForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("cnt") count: Int,
        @Query("units") units: String,
    ): ForecastResponse
}