package com.taingdev.weatherapp.domain.usecase

import com.taingdev.weatherapp.domain.data.CityRepository
import com.taingdev.weatherapp.domain.entity.City
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify


class RemoveFavoriteCityUseCaseTest {

    private lateinit var SUT: RemoveFavoriteCityUseCase

    private lateinit var cityRepositoryMock: CityRepository

    private val CITY_PARAM = City("", 1.0, 2.0, "")

    @Before
    fun setUp() {
        cityRepositoryMock = mock<CityRepository>()

        SUT = RemoveFavoriteCityUseCase(cityRepositoryMock)
    }

    @Test
    fun removeFavoriteCity_success_cityPassedToRepository() = runTest {
        val ac = argumentCaptor<City>()

        SUT.removeFavoriteCity(CITY_PARAM)

        verify(cityRepositoryMock, times(1)).removeFavoriteCity(ac.capture())
        val captures = ac.allValues
        Assert.assertEquals(CITY_PARAM, captures[0])
    }
}