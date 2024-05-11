package com.taingdev.weatherapp.di.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.taingdev.weatherapp.data.local.LocalFavoriteCityDataSource
import com.taingdev.weatherapp.data.remote.RemoteCityDataSource
import com.taingdev.weatherapp.data.remote.RemoteForecastDataSource
import com.taingdev.weatherapp.data.repositories.CityRepositoryImpl
import com.taingdev.weatherapp.data.repositories.ForecastRepositoryImpl
import com.taingdev.weatherapp.domain.data.CityRepository
import com.taingdev.weatherapp.domain.data.ForecastRepository
import com.taingdev.weatherapp.domain.usecase.CheckFavoriteCityUseCase
import com.taingdev.weatherapp.domain.usecase.FetchForecastUseCase
import com.taingdev.weatherapp.domain.usecase.GetFavoriteCitiesUseCase
import com.taingdev.weatherapp.domain.usecase.RemoveFavoriteCityUseCase
import com.taingdev.weatherapp.domain.usecase.SaveFavoriteCityUseCase
import com.taingdev.weatherapp.domain.usecase.SearchCitiesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    @Named("ioDispatcher")
    fun provideIoDispatcher() = Dispatchers.IO

    @Provides
    @Singleton
    fun provideSharedPreferences(context: Application): SharedPreferences =
        context.getSharedPreferences("weather_app", Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideCityRepository(
        remoteCityDataSource: RemoteCityDataSource,
        localFavoriteCityDataSource: LocalFavoriteCityDataSource
    ): CityRepository = CityRepositoryImpl(remoteCityDataSource, localFavoriteCityDataSource)

    @Provides
    @Singleton
    fun provideSearchCitiesUseCase(
        cityRepository: CityRepository,
    ) = SearchCitiesUseCase(cityRepository)

    @Provides
    @Singleton
    fun provideForecastRepository(
        remoteForecastDataSource: RemoteForecastDataSource
    ): ForecastRepository = ForecastRepositoryImpl(remoteForecastDataSource)

    @Provides
    @Singleton
    fun provideFetchForecastUseCase(
        forecastRepository: ForecastRepository,
    ) = FetchForecastUseCase(forecastRepository)

    @Provides
    @Singleton
    fun provideCheckFavoriteCityUseCase(
        cityRepository: CityRepository,
    ) = CheckFavoriteCityUseCase(cityRepository)

    @Provides
    @Singleton
    fun provideGetFavoriteCitiesUseCase(
        cityRepository: CityRepository,
    ) = GetFavoriteCitiesUseCase(cityRepository)

    @Provides
    @Singleton
    fun provideSaveFavoriteCityUseCase(
        cityRepository: CityRepository,
    ) = SaveFavoriteCityUseCase(cityRepository)

    @Provides
    @Singleton
    fun provideRemoveFavoriteCityUseCase(
        cityRepository: CityRepository,
    ) = RemoveFavoriteCityUseCase(cityRepository)
}