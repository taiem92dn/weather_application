package com.taingdev.weatherapp.data.model

import com.google.gson.annotations.SerializedName

data class ForecastResponse(
    @SerializedName("city") val cityResponse: CityResponse,
    val list: List<ForecastDatumResponse>,
)

data class ForecastDatumResponse (
    @SerializedName("dt") val date: Long,
    @SerializedName("temp") val temp: Temperature,
    @SerializedName("feels_like") val feelsLike: Temperature,
    val pressure: Int,
    val humidity: Int,
    val weather: List<Weather>,
    @SerializedName("speed") val windSpeed: Double, //m/s
    @SerializedName("deg") val windDegree: Int, // meteorological

)

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String,
)

data class Coord (
    val lon: Double,
    val lat: Double,
)

data class CityResponse (
    val id: Int,
    val name: String,
    val country: String,
    val coord: Coord,
)

data class Temperature(
    val day: Double,
    val min: Double,
    val max: Double,
    val night: Double,
    val eve: Double,
    val morn: Double,
)
