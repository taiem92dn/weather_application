package com.taingdev.weatherapp.domain.usecase

import com.taingdev.weatherapp.domain.data.ForecastRepository
import com.taingdev.weatherapp.domain.entity.City
import com.taingdev.weatherapp.domain.entity.Forecast
import com.taingdev.weatherapp.domain.entity.ForecastDatum
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

private const val LAT = 1.0

private const val LON = 2.0

class FetchForecastUseCaseTest {

    private lateinit var forecastRepositoryMock: ForecastRepository

    private lateinit var SUT: FetchForecastUseCase

    private val RESULT = Forecast(
        City("", LAT, LON, ""),
        listOf(
            ForecastDatum(
                date = 123,
                temperature = 2.0,
                feelsLike = 3.0,
                humidity = 4,
                windSpeed = 7.0,
                windDegree = 4,
                listOf("")
            )
        )
    )

    @Before
    fun setUp() = runTest {
        forecastRepositoryMock = mock<ForecastRepository>()

        SUT = FetchForecastUseCase(forecastRepositoryMock)

        success()
    }

    @Test
    fun fetchForecast_success_latLonPassedToRepository() = runTest {
        val ac = argumentCaptor<Double>()

        SUT.fetchForecast(LAT, LON)

        verify(forecastRepositoryMock, times(1)).fetchForecast(ac.capture(), ac.capture())
        val captures = ac.allValues
        assertEquals(captures[0], LAT, 0.toDouble())
        assertEquals(captures[1], LON, 0.toDouble())
    }

    @Test
    fun fetchForecast_success_successIsReturned() = runTest {
        val result = SUT.fetchForecast(LAT, LON)
        assertEquals(RESULT, result)
    }

    private suspend fun success() {
        `when`(forecastRepositoryMock.fetchForecast(any(), any())).thenReturn(
            RESULT
        )
    }

}