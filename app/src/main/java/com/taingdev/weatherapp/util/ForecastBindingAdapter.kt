package com.taingdev.weatherapp.util

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.taingdev.weatherapp.R
import com.taingdev.weatherapp.model.ForecastDatumItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ForecastBindingAdapter {

    @BindingAdapter("temperature")
    @JvmStatic
    fun setTemperature(textView: TextView, item: ForecastDatumItem?) {
        if (item == null) return
        textView.text = String.format("${item.temperature.toInt()}\u00B0")
    }

    @BindingAdapter("weather")
    @JvmStatic
    fun setWeather(textView: TextView, item: ForecastDatumItem?) {
        if (item == null) return
        textView.text = if (item.weather.isNotEmpty()) item.weather[0] else ""
    }

    @BindingAdapter("humidity")
    @JvmStatic
    fun setHumidity(textView: TextView, item: ForecastDatumItem?) {
        if (item == null) return
        textView.text = "${textView.context.getString(R.string.text_humidity)} ${item.humidity}%"
    }

    @BindingAdapter("feelsLike")
    @JvmStatic
    fun setFeelsLike(textView: TextView, item: ForecastDatumItem?) {
        if (item == null) return
        textView.text = textView.context.getString(R.string.text_feels_like, item.feelsLike.toInt())
    }

    @BindingAdapter("windSpeed")
    @JvmStatic
    fun setWindSpeed(textView: TextView, item: ForecastDatumItem?) {
        if (item == null) return
        textView.text = textView.context.getString(R.string.text_wind_speed, item.windSpeed)
    }

    @BindingAdapter("windDegree")
    @JvmStatic
    fun setWindDegree(textView: TextView, item: ForecastDatumItem?) {
        if (item == null) return
        textView.text = textView.context.getString(R.string.text_wind_degree, item.windDegree)
    }

    @BindingAdapter("forecastDate")
    @JvmStatic
    fun setForecastDate(textView: TextView, item: ForecastDatumItem?) {
        if (item == null) return
        val date = Date(item.date*1000)
        val format = SimpleDateFormat("EEE", Locale.getDefault())
        textView.text = format.format(date)
    }

    @BindingAdapter("windCondition")
    @JvmStatic
    fun setWindCondition(textView: TextView, item: ForecastDatumItem?) {
        if (item == null) return
        textView.text = textView.context.getString(
            R.string.text_wind_condition,
            item.windSpeed,
            item.windDegree
        )
    }

    @BindingAdapter("favoriteCity")
    @JvmStatic
    fun setFavoriteCity(imageView: ImageView, isFavorite: Boolean) {
        imageView.setImageResource(
            if (isFavorite) R.drawable.ic_favorite_24
            else R.drawable.ic_favorite_border_24
        )
    }
}