package com.taingdev.weatherapp.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taingdev.weatherapp.domain.usecase.GetFavoriteCitiesUseCase
import com.taingdev.weatherapp.extensions.mapTo
import com.taingdev.weatherapp.model.CityItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getFavoriteCitiesUseCase: GetFavoriteCitiesUseCase
): ViewModel() {

    private val _favoriteCities = MutableLiveData<List<CityItem>>()
    val favoriteCities: LiveData<List<CityItem>> = _favoriteCities

    init {
        handleGetFavoriteCities()
    }

    fun handleGetFavoriteCities() {
        viewModelScope.launch {
            _favoriteCities.value = getFavoriteCitiesUseCase.getFavoriteCities().map {
                it.mapTo()
            }
        }
    }
}