package com.taingdev.weatherapp.extensions

import com.taingdev.weatherapp.domain.entity.City
import com.taingdev.weatherapp.domain.entity.Forecast
import com.taingdev.weatherapp.domain.entity.ForecastDatum
import com.taingdev.weatherapp.model.CityItem
import com.taingdev.weatherapp.model.ForecastDatumItem
import com.taingdev.weatherapp.model.ForecastItem

fun City.mapTo(): CityItem {
    return CityItem(
        name = name,
        lat = lat,
        lon = lon,
        country = country
    )
}

fun CityItem.mapTo(): City {
    return City(
        name = name,
        lat = lat,
        lon = lon,
        country = country
    )
}

fun Forecast.mapTo(): ForecastItem {
    return ForecastItem(
        city = city.mapTo(),
        data = data.map { it.mapTo() }
    )
}

fun ForecastDatum.mapTo(): ForecastDatumItem {
    return ForecastDatumItem(
        date = date,
        temperature = temperature,
        feelsLike = feelsLike,
        humidity = humidity,
        windSpeed = windSpeed,
        windDegree = windDegree,
        weather = weather
    )
}
