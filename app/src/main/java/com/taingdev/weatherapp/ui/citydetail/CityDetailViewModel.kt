package com.taingdev.weatherapp.ui.citydetail

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taingdev.weatherapp.R
import com.taingdev.weatherapp.domain.usecase.CheckFavoriteCityUseCase
import com.taingdev.weatherapp.domain.usecase.FetchForecastUseCase
import com.taingdev.weatherapp.domain.usecase.RemoveFavoriteCityUseCase
import com.taingdev.weatherapp.domain.usecase.SaveFavoriteCityUseCase
import com.taingdev.weatherapp.extensions.mapTo
import com.taingdev.weatherapp.model.CityItem
import com.taingdev.weatherapp.model.ForecastItem
import com.taingdev.weatherapp.network.ApiResource
import com.taingdev.weatherapp.network.INetworkCheckService
import com.taingdev.weatherapp.ui.BaseApiViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CityDetailViewModel @Inject constructor(
    private val context: Application,
    private val fetchForecastUseCase: FetchForecastUseCase,
    private val checkFavoriteCityUseCase: CheckFavoriteCityUseCase,
    private val saveFavoriteCityUseCase: SaveFavoriteCityUseCase,
    private val removeFavoriteCityUseCase: RemoveFavoriteCityUseCase,
    private val networkCheckService: INetworkCheckService,
) : BaseApiViewModel() {
    private val _forecastItemLiveData = MutableLiveData<ApiResource<ForecastItem>>()
    val forecastItemLiveData: LiveData<ApiResource<ForecastItem>>
        get() = _forecastItemLiveData

    val isFavoriteCity = MutableLiveData(false)

    var cityItem: CityItem? = null
        set(value) {
            field = value
            if (value != null) {
                handleFetchData(value)

                handleCheckFavoriteCity(value)
            }
        }

    fun handleCheckFavoriteCity(cityItem: CityItem) {
        viewModelScope.launch {
            isFavoriteCity.value = checkFavoriteCityUseCase.checkFavoriteCity(cityItem.mapTo())
        }
    }

    fun handleSaveFavoriteCity() {
        viewModelScope.launch {
            cityItem?.let {
                saveFavoriteCityUseCase.saveFavoriteCity(it.mapTo())
                isFavoriteCity.value = true
            }
        }
    }

    fun handleRemoveFavoriteCity() {
        viewModelScope.launch {
            cityItem?.let {
                removeFavoriteCityUseCase.removeFavoriteCity(it.mapTo())
                isFavoriteCity.value = false
            }
        }
    }
    fun handleFetchData(cityItem: CityItem) {
        viewModelScope.launch {
            showError.value = false

            if (networkCheckService.hasInternet()) {
                showLoading.value = true
                _forecastItemLiveData.value = ApiResource.Loading()

                _forecastItemLiveData.value = try {
                    ApiResource.Success(
                        fetchForecastUseCase.fetchForecast(
                            cityItem.lat, cityItem.lon
                        ).mapTo()
                    )
                } catch (e: Throwable) {
                    e.printStackTrace()
                    showError(context.getString(R.string.text_something_went_wrong))
                    ApiResource.Error(e.message)
                }

            } else {
                _forecastItemLiveData.value = ApiResource.NoInternet()
                showError(context.getString(R.string.no_internet))
            }
            showLoading.value = false
        }
    }
}