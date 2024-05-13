package com.taingdev.weatherapp.utils

import com.taingdev.weatherapp.domain.entity.City
import com.taingdev.weatherapp.domain.entity.Forecast
import com.taingdev.weatherapp.domain.entity.ForecastDatum
import com.taingdev.weatherapp.model.CityItem
import com.taingdev.weatherapp.model.ForecastDatumItem
import com.taingdev.weatherapp.model.ForecastItem
import com.taingdev.weatherapp.network.INetworkCheckService
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response


class NetworkCheckServiceFake(var result: Boolean = false) : INetworkCheckService {

    override fun hasInternet(): Boolean {
        return result
    }
}
fun makeErrorResult(result: String): HttpException {
    return HttpException(
        Response.error<String>(
            500,
            "\"$result\"".toResponseBody("application/json".toMediaType())
        )
    )
}


const val LAT = 1.0

const val LON = 2.0
fun provideCity() = City("", LAT, LON, "")
fun provideCityItem() = CityItem("", LAT, LON, "")
fun provideCityList() = listOf(provideCity())
fun provideCityItemList() = listOf(provideCityItem())

fun provideForecast() = Forecast(
    provideCity(),
    listOf(provideForecastDatum())
)

private const val date = 123L

fun provideForecastDatum() =
    ForecastDatum(
        date = date,
        temperature = 2.0,
        feelsLike = 3.0,
        humidity = 4,
        windSpeed = 7.0,
        windDegree = 4,
        listOf("")
    )

fun provideForecastItem() = ForecastItem(
    provideCityItem(),
    listOf(provideForecastDatumItem())
)

fun provideForecastDatumItem() = ForecastDatumItem(
    date = date,
    temperature = 2.0,
    feelsLike = 3.0,
    humidity = 4,
    windSpeed = 7.0,
    windDegree = 4,
    listOf("")
)
