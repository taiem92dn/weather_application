package com.taingdev.weatherapp.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.cardview.widget.CardView
import com.taingdev.weatherapp.databinding.LayoutForecastDatumBinding
import com.taingdev.weatherapp.model.ForecastDatumItem
import com.taingdev.weatherapp.util.ForecastBindingAdapter

class ForecastDatumView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : CardView(context, attrs, defStyleAttr) {

    var forecastDatum: ForecastDatumItem? = null
        set(value) {
            field = value
            bindData()
        }

    var binding: LayoutForecastDatumBinding? = null

    init {
        binding = LayoutForecastDatumBinding.inflate(
            LayoutInflater.from(context), this, true
        )
    }

    private fun bindData() {
        forecastDatum?.let {
            binding?.apply {
                ForecastBindingAdapter.setForecastDate(
                    tvDate, it
                )
                ForecastBindingAdapter.setTemperature(
                    tvTemperature, it
                )
                ForecastBindingAdapter.setHumidity(
                    tvHumidity, it
                )
                ForecastBindingAdapter.setWindCondition(
                    tvWindCondition, it
                )
            }
        }
    }
}