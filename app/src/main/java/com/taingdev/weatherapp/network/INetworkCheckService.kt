package com.taingdev.weatherapp.network

interface INetworkCheckService {
    fun hasInternet(): Boolean
}