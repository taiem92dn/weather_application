package com.taingdev.weatherapp.domain.usecase

import com.taingdev.weatherapp.domain.data.CityRepository
import com.taingdev.weatherapp.domain.entity.City
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

private const val CITY_QUERY = "city"

class SearchCitiesUseCaseTest {

    private lateinit var SUT: SearchCitiesUseCase

    private lateinit var cityRepositoryMock: CityRepository

    private val CITY_RESULT = emptyList<City>()

    @Before
    fun setUp() = runTest {
        cityRepositoryMock = mock<CityRepository>()

        SUT = SearchCitiesUseCase(cityRepositoryMock)

        success()
    }

    @Test
    fun searchCities_success_queryPassedToRepository() = runTest {
        val ac = argumentCaptor<String>()
        SUT.searchCities(CITY_QUERY)
        verify(cityRepositoryMock, times(1)).searchCities(ac.capture())
        val captures = ac.allValues
        assertEquals(captures[0], CITY_QUERY)
    }

    @Test
    fun searchCities_success_successIsReturned() = runTest {
        val result = SUT.searchCities(CITY_QUERY)
        assertEquals(CITY_RESULT, result)
    }

    private suspend fun success() {
        `when`(cityRepositoryMock.searchCities(any())).thenReturn(
            CITY_RESULT
        )
    }
}