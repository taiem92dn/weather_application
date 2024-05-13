package com.taingdev.weatherapp.ui.search

import android.app.Application
import org.junit.Rule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.taingdev.weatherapp.domain.data.CityRepository
import com.taingdev.weatherapp.domain.entity.City
import com.taingdev.weatherapp.domain.usecase.SearchCitiesUseCase
import com.taingdev.weatherapp.model.CityItem
import com.taingdev.weatherapp.network.ApiResource
import com.taingdev.weatherapp.network.INetworkCheckService
import com.taingdev.weatherapp.utils.NetworkCheckServiceFake
import com.taingdev.weatherapp.utils.makeErrorResult
import com.taingdev.weatherapp.utils.provideCityItemList
import com.taingdev.weatherapp.utils.provideCityList
import com.tngdev.movieapp.utils.getValueForTest
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import retrofit2.HttpException
import retrofit2.Response


private const val QUERY = "query"

class SearchViewModelTest {

    @get:Rule
    val instanttaskexecutor = InstantTaskExecutorRule()

    private lateinit var SUT: SearchViewModel

    private lateinit var applicationMock: Application

    private lateinit var useCase: SearchCitiesUseCase

    private lateinit var cityRepositoryFake: CityRepositoryFake

    private lateinit var networkCheckServiceFake: NetworkCheckServiceFake


    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {

        Dispatchers.setMain(Dispatchers.Unconfined)

        applicationMock = mock<Application>()
        Mockito.`when`(applicationMock.getString(any())).thenReturn("")

        cityRepositoryFake = CityRepositoryFake()
        useCase = SearchCitiesUseCase(cityRepositoryFake)
        networkCheckServiceFake = NetworkCheckServiceFake()

        SUT = SearchViewModel(
            applicationMock,
            useCase,
            networkCheckServiceFake
        )

        success()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun handleSearch_success_queryPassedToUseCase() = runTest {
        SUT.handleSearch(QUERY)

        cityRepositoryFake.sendCompletionToAllCurrentRequests(RESULT_CITIES)

        Assert.assertEquals(QUERY, cityRepositoryFake.passedParam)
        Assert.assertEquals(1, cityRepositoryFake.callCount)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun handleSearch_success_successfulResultReturnedAndLoadingShownThenHidden() = runTest {
        val values = mutableListOf<ApiResource<List<CityItem>>>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            SUT.cities.toList(values)
        }

        SUT.handleSearch(QUERY)

        Assert.assertTrue(
            "First value is not ApiResource.Loading",
            values[1] is ApiResource.Loading
        )
        Assert.assertEquals(true, SUT.showLoading.value)

        cityRepositoryFake.sendCompletionToAllCurrentRequests(RESULT_CITIES)

        Assert.assertEquals(values[2].data, RESULT_CITY_ITEMS)
        Assert.assertEquals(false, SUT.showLoading.value)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun handleSearch_error_errorResultReturnedAndLoadingShownThenHidden() = runTest {
        val values = mutableListOf<ApiResource<List<CityItem>>>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            SUT.cities.toList(values)
        }

        SUT.handleSearch(QUERY)

        Assert.assertTrue(
            "First value is not ApiResource.Loading",
            values[1] is ApiResource.Loading
        )
        Assert.assertEquals(true, SUT.showLoading.value)

        cityRepositoryFake.sendErrorToCurrentRequests(makeErrorResult("Something went wrong"))

        Assert.assertTrue("Last value is not error", values[2] is ApiResource.Error)
        Assert.assertEquals(false, SUT.showLoading.value)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun handleSearch_noInternet_noInternetResultReturnedAndLoadingHidden() = runTest {
        noInternet()

        val values = mutableListOf<ApiResource<List<CityItem>>>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            SUT.cities.toList(values)
        }

        SUT.handleSearch(QUERY)

        Assert.assertTrue(
            "First value is not ApiResource.NoInternet",
            values[1] is ApiResource.NoInternet
        )
        Assert.assertEquals(false, SUT.showLoading.value)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun handleSearch_noInternet_useCaseNotCalled() = runTest {
        noInternet()

        SUT.handleSearch(QUERY)

        Assert.assertEquals(0, cityRepositoryFake.callCount)
    }

    private fun success() {
        networkCheckServiceFake.result = true
    }

    private fun noInternet() {
        networkCheckServiceFake.result = false
    }


    class CityRepositoryFake : CityRepository {

        private val completable = CompletableDeferred<List<City>>()

        var callCount = 0
        var passedParam = ""

        fun sendCompletionToAllCurrentRequests(result: List<City>) {
            completable.complete(result)
        }

        fun sendErrorToCurrentRequests(throwable: Throwable) {
            completable.completeExceptionally(throwable)
        }

        override suspend fun searchCities(query: String): List<City> {
            passedParam = query
            callCount++
            return completable.await()
        }

        override suspend fun getFavoriteCities(): List<City> {
            TODO("Not yet implemented")
        }

        override suspend fun saveFavoriteCity(city: City) {
            TODO("Not yet implemented")
        }

        override suspend fun removeFavoriteCity(city: City) {
            TODO("Not yet implemented")
        }

        override suspend fun checkFavoriteCity(city: City): Boolean {
            TODO("Not yet implemented")
        }

    }

    companion object {
        val RESULT_CITIES = provideCityList()
        val RESULT_CITY_ITEMS = provideCityItemList()
    }
}