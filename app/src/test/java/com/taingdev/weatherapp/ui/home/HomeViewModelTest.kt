package com.taingdev.weatherapp.ui.home

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.taingdev.weatherapp.domain.usecase.GetFavoriteCitiesUseCase
import com.taingdev.weatherapp.ui.citydetail.CityDetailViewModel
import com.taingdev.weatherapp.utils.provideCityItemList
import com.taingdev.weatherapp.utils.provideCityList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify


class HomeViewModelTest {

    private val CITY_ITEM_LIST = provideCityItemList()
    private lateinit var getFavoriteCitiesUseCaseMock: GetFavoriteCitiesUseCase

    @get:Rule
    val instanttaskexecutor = InstantTaskExecutorRule()

    private lateinit var SUT: HomeViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() = runTest {
        Dispatchers.setMain(Dispatchers.Unconfined)
        getFavoriteCitiesUseCaseMock = mock<GetFavoriteCitiesUseCase>()

        success()

        SUT = HomeViewModel(getFavoriteCitiesUseCaseMock)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun handleGetFavoriteCities_success_useCaseCalledDuringInitializing() = runTest {
        verify(getFavoriteCitiesUseCaseMock, times(1)).getFavoriteCities()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun handleGetFavoriteCities_success_useCaseCalledTwice() = runTest {
        SUT.handleGetFavoriteCities()

        verify(getFavoriteCitiesUseCaseMock, times(2)).getFavoriteCities()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun handleGetFavoriteCities_success_resultPassedToLiveData() = runTest {
        SUT.handleGetFavoriteCities()

        verify(getFavoriteCitiesUseCaseMock, times(2)).getFavoriteCities()

        Assert.assertEquals(CITY_ITEM_LIST, SUT.favoriteCities.value)
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private suspend fun success() {
        Mockito.`when`(getFavoriteCitiesUseCaseMock.getFavoriteCities())
            .thenReturn(provideCityList())
    }
}