package com.taingdev.weatherapp.ui.citydetail

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.taingdev.weatherapp.domain.data.CityRepository
import com.taingdev.weatherapp.domain.data.ForecastRepository
import com.taingdev.weatherapp.domain.entity.City
import com.taingdev.weatherapp.domain.entity.Forecast
import com.taingdev.weatherapp.domain.usecase.CheckFavoriteCityUseCase
import com.taingdev.weatherapp.domain.usecase.FetchForecastUseCase
import com.taingdev.weatherapp.domain.usecase.RemoveFavoriteCityUseCase
import com.taingdev.weatherapp.domain.usecase.SaveFavoriteCityUseCase
import com.taingdev.weatherapp.network.ApiResource
import com.taingdev.weatherapp.utils.LAT
import com.taingdev.weatherapp.utils.LON
import com.taingdev.weatherapp.utils.NetworkCheckServiceFake
import com.taingdev.weatherapp.utils.makeErrorResult
import com.taingdev.weatherapp.utils.provideCity
import com.taingdev.weatherapp.utils.provideCityItem
import com.taingdev.weatherapp.utils.provideForecast
import com.taingdev.weatherapp.utils.provideForecastItem
import kotlinx.coroutines.CompletableDeferred
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
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify


private const val CHECK_RESULT = true

class CityDetailViewModelTest {


    private val CITY: City = provideCity()
    private lateinit var cityRepositoryMock: CityRepository
    private lateinit var forecastRepositoryFake: ForecastRepositoryFake
    private lateinit var removeFavoriteCityUseCaseMock: RemoveFavoriteCityUseCase
    private lateinit var saveFavoriteCityUseCaseMock: SaveFavoriteCityUseCase
    private lateinit var checkFavoriteCityUseCaseMock: CheckFavoriteCityUseCase
    private lateinit var fetchForecastUseCase: FetchForecastUseCase

    @get:Rule
    val instanttaskexecutor = InstantTaskExecutorRule()

    private lateinit var SUT: CityDetailViewModel

    private lateinit var applicationMock: Application

    private lateinit var networkCheckServiceFake: NetworkCheckServiceFake

    private val CITY_ITEM_PARAM = provideCityItem()

    private val FORECAST = provideForecast()

    private val RESULT_FORECASTITEM = provideForecastItem()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() = runTest {
        Dispatchers.setMain(Dispatchers.Unconfined)

        applicationMock = mock<Application>()
        Mockito.`when`(applicationMock.getString(any())).thenReturn("")

        networkCheckServiceFake = NetworkCheckServiceFake()

        forecastRepositoryFake = ForecastRepositoryFake()
        fetchForecastUseCase = FetchForecastUseCase(forecastRepositoryFake)

        cityRepositoryMock = mock<CityRepository>()
        removeFavoriteCityUseCaseMock = mock<RemoveFavoriteCityUseCase>()
        saveFavoriteCityUseCaseMock = mock<SaveFavoriteCityUseCase>()
        checkFavoriteCityUseCaseMock = mock<CheckFavoriteCityUseCase>()

        SUT = CityDetailViewModel(
            applicationMock,
            fetchForecastUseCase,
            checkFavoriteCityUseCaseMock,
            saveFavoriteCityUseCaseMock,
            removeFavoriteCityUseCaseMock,
            networkCheckServiceFake
        )

        success()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun handleFetchData_success_paramsPassedToFetchForecastUseCase() = runTest {
        SUT.handleFetchData(CITY_ITEM_PARAM)

        forecastRepositoryFake.sendCompletionToAllCurrentRequests(FORECAST)

        Assert.assertEquals(LAT, forecastRepositoryFake.recordedLat, 0.0)
        Assert.assertEquals(LON, forecastRepositoryFake.recordedLon, 0.0)
        Assert.assertEquals(1, forecastRepositoryFake.callCount)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun handleFetchData_success_successfulResultReturnedAndLoadingShownThenHidden() = runTest {
        SUT.handleFetchData(CITY_ITEM_PARAM)

        Assert.assertTrue(
            "Current value is not ApiResource.Loading",
            SUT.forecastItemLiveData.value is ApiResource.Loading
        )
        Assert.assertEquals(CHECK_RESULT, SUT.showLoading.value)

        forecastRepositoryFake.sendCompletionToAllCurrentRequests(FORECAST)

        Assert.assertEquals(SUT.forecastItemLiveData.value?.data, RESULT_FORECASTITEM)
        Assert.assertEquals(false, SUT.showLoading.value)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun handleFetchData_error_errorResultReturnedAndLoadingShownThenHidden() = runTest {
        SUT.handleFetchData(CITY_ITEM_PARAM)

        Assert.assertTrue(
            "Current value is not ApiResource.Loading",
            SUT.forecastItemLiveData.value is ApiResource.Loading
        )
        Assert.assertEquals(CHECK_RESULT, SUT.showLoading.value)

        forecastRepositoryFake.sendErrorToCurrentRequests(makeErrorResult("Something went wrong"))

        Assert.assertTrue("Current value is not error", SUT.forecastItemLiveData.value is ApiResource.Error)
        Assert.assertEquals(false, SUT.showLoading.value)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun handleFetchData_noInternet_noInternetResultReturnedAndLoadingHidden() = runTest {
        noInternet()

        SUT.handleFetchData(CITY_ITEM_PARAM)

        Assert.assertTrue(
            "Current value is not ApiResource.NoInternet",
            SUT.forecastItemLiveData.value is ApiResource.NoInternet
        )
        Assert.assertEquals(false, SUT.showLoading.value)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun handleFetchData_noInternet_useCaseNotCalled() = runTest {
        noInternet()

        SUT.handleFetchData(CITY_ITEM_PARAM)

        Assert.assertEquals(0, forecastRepositoryFake.callCount)
    }

    // handleCheckFavoriteCity
    @ExperimentalCoroutinesApi
    @Test
    fun handleCheckFavoriteCity_success_cityParamPassedToUseCase() = runTest {
        val ac = argumentCaptor<City>()

        SUT.handleCheckFavoriteCity(CITY_ITEM_PARAM)

        verify(checkFavoriteCityUseCaseMock, times(1)).checkFavoriteCity(ac.capture())

        Assert.assertEquals(CITY, ac.firstValue)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun handleCheckFavoriteCity_success_resultPassedToLiveData() = runTest {
        SUT.handleCheckFavoriteCity(CITY_ITEM_PARAM)

        Assert.assertEquals(CHECK_RESULT, SUT.isFavoriteCity.value)
    }

    // handleSaveFavoriteCity
    @ExperimentalCoroutinesApi
    @Test
    fun handleSaveFavoriteCity_success_cityParamPassedToUseCase() = runTest {
        val ac = argumentCaptor<City>()

        SUT.cityItem = CITY_ITEM_PARAM
        SUT.handleSaveFavoriteCity()

        verify(saveFavoriteCityUseCaseMock, times(1)).saveFavoriteCity(ac.capture())

        Assert.assertEquals(CITY, ac.firstValue)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun handleSaveFavoriteCity_success_liveDataUpdatedToTrue() = runTest {
        SUT.cityItem = CITY_ITEM_PARAM
        SUT.handleSaveFavoriteCity()

        Assert.assertEquals(true, SUT.isFavoriteCity.value)
    }

    // handleRemoveFavoriteCity
    @ExperimentalCoroutinesApi
    @Test
    fun handleRemoveFavoriteCity_success_cityParamPassedToUseCase() = runTest {
        val ac = argumentCaptor<City>()

        SUT.cityItem = CITY_ITEM_PARAM
        SUT.handleRemoveFavoriteCity()

        verify(removeFavoriteCityUseCaseMock, times(1)).removeFavoriteCity(ac.capture())

        Assert.assertEquals(CITY, ac.firstValue)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun handleRemoveFavoriteCity_success_liveDataUpdatedToFalse() = runTest {
        SUT.cityItem = CITY_ITEM_PARAM
        SUT.handleRemoveFavoriteCity()

        Assert.assertEquals(false, SUT.isFavoriteCity.value)
    }

    // setCityItem
    @ExperimentalCoroutinesApi
    @Test
    fun setCityItem_success_fetchUseCaseAndCheckUseCaseCalled() = runTest {
        SUT.cityItem = CITY_ITEM_PARAM

        Assert.assertEquals(1, forecastRepositoryFake.callCount)
        verify(checkFavoriteCityUseCaseMock, times(1)).checkFavoriteCity(any())
    }



    private suspend fun success() {
        networkCheckServiceFake.result = CHECK_RESULT

        Mockito.`when`(checkFavoriteCityUseCaseMock.checkFavoriteCity(any())).thenReturn(CHECK_RESULT)
    }

    private fun noInternet() {
        networkCheckServiceFake.result = false
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    class ForecastRepositoryFake : ForecastRepository {

        private val completable = CompletableDeferred<Forecast>()

        var callCount = 0
        var recordedLat = 0.0
        var recordedLon = 0.0

        fun sendCompletionToAllCurrentRequests(result: Forecast) {
            completable.complete(result)
        }

        fun sendErrorToCurrentRequests(throwable: Throwable) {
            completable.completeExceptionally(throwable)
        }

        override suspend fun fetchForecast(lat: Double, lon: Double): Forecast {
            recordedLat = lat
            recordedLon = lon
            callCount++
            return completable.await()
        }


    }

}