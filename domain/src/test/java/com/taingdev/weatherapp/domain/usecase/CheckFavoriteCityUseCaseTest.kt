package com.taingdev.weatherapp.domain.usecase

import com.taingdev.weatherapp.domain.data.CityRepository
import com.taingdev.weatherapp.domain.entity.City
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock


class CheckFavoriteCityUseCaseTest {

    private lateinit var SUT: CheckFavoriteCityUseCase

    private lateinit var cityRepositoryFake: CityRepositoryFake

    private val CITY_PARAM = City("", 1.0, 2.0, "")

    @Before
    fun setUp() {
        cityRepositoryFake = CityRepositoryFake()

        SUT = CheckFavoriteCityUseCase(cityRepositoryFake)
    }

    @Test
    fun checkFavoriteCity_success_cityPassedToRepository() = runTest {

        SUT.checkFavoriteCity(CITY_PARAM)

        Assert.assertEquals(1, cityRepositoryFake.callCount)
        Assert.assertEquals(CITY_PARAM, cityRepositoryFake.cityParam)
    }

    @Test
    fun checkFavoriteCity_success_returnsTrue() = runTest {
        success(true)

        val result = SUT.checkFavoriteCity(CITY_PARAM)

        Assert.assertEquals(true, result)
    }

    @Test
    fun checkFavoriteCity_success_returnsFalse() = runTest {
        success(false)

        val result = SUT.checkFavoriteCity(CITY_PARAM)

        Assert.assertEquals(false, result)
    }

    private fun success(result: Boolean) {
        cityRepositoryFake.checkResult = result
    }


    class CityRepositoryFake: CityRepository {

        var callCount = 0
        var cityParam: City? = null
        var checkResult = false

        override suspend fun searchCities(query: String): List<City> {
            TODO("Not yet implemented")
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
            callCount++
            cityParam = city

            return checkResult
        }

    }

}