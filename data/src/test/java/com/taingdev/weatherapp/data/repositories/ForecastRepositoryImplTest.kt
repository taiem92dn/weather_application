package com.taingdev.weatherapp.data.repositories

import com.taingdev.weatherapp.data.remote.RemoteForecastDataSource
import com.taingdev.weatherapp.data.utils.provideForecast
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.argumentCaptor

private const val LAT = 1.0

private const val LON = 2.0

@RunWith(MockitoJUnitRunner::class)
class ForecastRepositoryImplTest {

    private val FORECAST = provideForecast()
    private lateinit var remoteForecastDataSourceMock: RemoteForecastDataSource
    lateinit var SUT: ForecastRepositoryImpl

    @Before
    fun setup() = runTest {
        remoteForecastDataSourceMock = mock()

        SUT = ForecastRepositoryImpl(remoteForecastDataSourceMock)

        success()
    }

    private suspend fun success() {
        Mockito.`when`(remoteForecastDataSourceMock.fetchForecast(LAT, LON))
            .thenReturn(FORECAST)
    }

    @Test
    fun fetchForecast_success_remoteDataSourceCalled() = runTest {
        val ac = argumentCaptor<Double>()

        val result = SUT.fetchForecast(LAT, LON)

        verify(remoteForecastDataSourceMock, times(1)).fetchForecast(ac.capture(), ac.capture())
        Assert.assertEquals(ac.firstValue, LAT, 0.0)
        Assert.assertEquals(ac.secondValue, LON, 0.0)
        Assert.assertEquals(FORECAST, result)
    }

}