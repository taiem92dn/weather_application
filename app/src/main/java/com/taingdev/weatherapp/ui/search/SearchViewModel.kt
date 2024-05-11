package com.taingdev.weatherapp.ui.search

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taingdev.weatherapp.R
import com.taingdev.weatherapp.domain.entity.City
import com.taingdev.weatherapp.domain.usecase.SearchCitiesUseCase
import com.taingdev.weatherapp.extensions.mapTo
import com.taingdev.weatherapp.model.CityItem
import com.taingdev.weatherapp.network.ApiResource
import com.taingdev.weatherapp.network.INetworkCheckService
import com.taingdev.weatherapp.ui.BaseApiViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val context: Application,
    private val searchCitiesUseCase: SearchCitiesUseCase,
    private val networkCheckService: INetworkCheckService,
): BaseApiViewModel() {

    private var _cities = MutableStateFlow<ApiResource<List<CityItem>>>(ApiResource.Success(emptyList()))
    val cities: StateFlow<ApiResource<List<CityItem>>> = _cities

    fun handleSearch(query: String) {
        viewModelScope.launch {
            showError.value = false

            if (networkCheckService.hasInternet()) {
                showLoading.value = true
                _cities.value = ApiResource.Loading()

                _cities.value = try {
                    ApiResource.Success(
                        searchCitiesUseCase.searchCities(query).map {
                            it.mapTo()
                        }
                    )
                } catch (e: Throwable) {
                    e.printStackTrace()
                    showError(context.getString(R.string.text_something_went_wrong))
                    ApiResource.Error(e.message)
                }

            }
            else {
                _cities.value = ApiResource.NoInternet()
                showError(context.getString(R.string.no_internet))
            }
            showLoading.value = false
        }
    }

}