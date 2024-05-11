package com.taingdev.weatherapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseApiViewModel: ViewModel() {

    val showLoading = MutableLiveData(false)
    val showError = MutableLiveData(false)
    val errorMessage = MutableLiveData("")


    fun showError(message: String) {
        showError.value = true
        errorMessage.value = message
    }
}