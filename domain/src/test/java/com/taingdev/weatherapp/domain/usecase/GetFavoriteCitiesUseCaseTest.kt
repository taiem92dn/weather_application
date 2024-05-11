package com.taingdev.weatherapp.domain.usecase

import com.taingdev.weatherapp.domain.data.CityRepository
import com.taingdev.weatherapp.domain.entity.City
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.MockitoKotlinException
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify


class GetFavoriteCitiesUseCaseTest {

    private lateinit var cityRepositoryMock: CityRepository

    private lateinit var SUT: GetFavoriteCitiesUseCase

    private val RESULT = listOf( City("London", 1.0, 2.0, "GB"))
    @Before
    fun setUp() = runTest {
        cityRepositoryMock = mock<CityRepository>()

        SUT = GetFavoriteCitiesUseCase(cityRepositoryMock)

        success()
    }

    @Test
    fun getFavoriteCities_success_repositoryCalled() = runTest{
        SUT.getFavoriteCities()

        verify(cityRepositoryMock, times(1)).getFavoriteCities()
    }

    @Test
    fun getFavoriteCities_success_successfulResultReturned() = runTest {
        val result = SUT.getFavoriteCities()

        Assert.assertEquals(RESULT, result)
    }

    private suspend fun success() {
        Mockito.`when`(cityRepositoryMock.getFavoriteCities()).thenReturn(
            RESULT
        )
    }
}