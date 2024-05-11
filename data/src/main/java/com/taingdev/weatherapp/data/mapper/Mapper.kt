package com.taingdev.weatherapp.data.mapper

import com.taingdev.weatherapp.domain.entity.City
import com.taingdev.weatherapp.domain.entity.Forecast
import com.taingdev.weatherapp.domain.entity.ForecastDatum
import com.taingdev.weatherapp.data.model.CityQuery
import com.taingdev.weatherapp.data.model.ForecastResponse

fun CityQuery.mapTo(): City {
    return City(
        name = name,
        lat = lat,
        lon = lon,
        country = country
    )
}

fun ForecastResponse.mapTo(): Forecast {
    return Forecast(
        city = City(
            name = cityResponse.name,
            lat = cityResponse.coord.lat,
            lon = cityResponse.coord.lon,
            country = cityResponse.country
        ),
       data = list.map {
           ForecastDatum(
               date = it.date,
               temperature = it.temp.day,
               feelsLike = it.feelsLike.day,
               humidity = it.humidity,
               windSpeed = it.windSpeed,
               windDegree = it.windDegree,
               weather = it.weather.map {w ->
                   w.description
               }
           )

       }
    )
}