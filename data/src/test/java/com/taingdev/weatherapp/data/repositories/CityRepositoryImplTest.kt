package com.taingdev.weatherapp.data.repositories

import com.taingdev.weatherapp.data.local.LocalFavoriteCityDataSource
import com.taingdev.weatherapp.data.remote.RemoteCityDataSource
import com.taingdev.weatherapp.data.utils.provideCity
import com.taingdev.weatherapp.data.utils.provideCityList
import com.taingdev.weatherapp.domain.entity.City
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify


private const val QUERY = "query"

private const val CHECK_RESULT = true

class CityRepositoryImplTest {

    private val CITY: City = provideCity()
    private val CITIES = provideCityList()
    private lateinit var localFavoriteCityDataSourceMock: LocalFavoriteCityDataSource
    private lateinit var remoteCityDataSourceMock: RemoteCityDataSource
    private lateinit var SUT: CityRepositoryImpl

    @Before
    fun setUp() = runTest {
        remoteCityDataSourceMock = mock()
        localFavoriteCityDataSourceMock = mock()

        SUT = CityRepositoryImpl(
            remoteCityDataSourceMock,
            localFavoriteCityDataSourceMock,
        )

        success()
    }

    private suspend fun success() {
        Mockito.`when`(remoteCityDataSourceMock.searchCities(QUERY)).thenReturn(CITIES)
        Mockito.`when`(localFavoriteCityDataSourceMock.getFavoriteCities()).thenReturn(CITIES)
        Mockito.`when`(localFavoriteCityDataSourceMock.checkFavoriteCity(CITY)).thenReturn(
            CHECK_RESULT
        )
    }

    @Test
    fun searchCities_success_remoteCityDataSourceCalled() = runTest {
        val ac = argumentCaptor<String>()

        val result = SUT.searchCities(QUERY)

        verify(remoteCityDataSourceMock, times(1)).searchCities(ac.capture())
        Assert.assertEquals(QUERY, ac.firstValue)
        Assert.assertEquals(CITIES, result)
    }

    @Test
    fun getFavoriteCities_success_localFavoriteCityDataSourceCalled() = runTest {
        val result = SUT.getFavoriteCities()

        verify(localFavoriteCityDataSourceMock, times(1)).getFavoriteCities()
        Assert.assertEquals(CITIES, result)
    }

    @Test
    fun saveFavoriteCity_success_localFavoriteCityDataSourceCalled() = runTest {
        val ac = argumentCaptor<City>()

        SUT.saveFavoriteCity(CITY)

        verify(localFavoriteCityDataSourceMock, times(1)).saveFavoriteCity(ac.capture())
        Assert.assertEquals(CITY, ac.firstValue)
    }

    @Test
    fun removeFavoriteCity_success_localFavoriteCityDataSourceCalled() = runTest {
        val ac = argumentCaptor<City>()

        SUT.removeFavoriteCity(CITY)

        verify(localFavoriteCityDataSourceMock, times(1)).removeFavoriteCity(ac.capture())
        Assert.assertEquals(CITY, ac.firstValue)
    }

    @Test
    fun checkFavoriteCity_success_localFavoriteCityDataSourceCalled() = runTest {
        val ac = argumentCaptor<City>()

        val result = SUT.checkFavoriteCity(CITY)

        verify(localFavoriteCityDataSourceMock, times(1)).checkFavoriteCity(ac.capture())
        Assert.assertEquals(CITY, ac.firstValue)
        Assert.assertEquals(CHECK_RESULT, result)
    }

}
