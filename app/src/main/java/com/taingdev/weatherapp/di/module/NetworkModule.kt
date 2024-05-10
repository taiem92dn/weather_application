package com.taingdev.weatherapp.di.module

import android.app.Application
import com.taingdev.weatherapp.BuildConfig
import com.taingdev.weatherapp.network.INetworkCheckService
import com.taingdev.weatherapp.util.Utils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    @Named("api_key")
    fun provideApiKey() = BuildConfig.API_KEY

    @Provides
    @Singleton
    fun provideOkHttpClient(
        @Named("api_key") apiKey: String,
    ): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC)

        val addQueryTokenInterceptor = Interceptor { chain ->
            val original = chain.request()
            val url = original.url.newBuilder()
                .addQueryParameter("api_key", apiKey)
                .addQueryParameter("language", "en-US")
                .build()
            val request = original.newBuilder().url(url).build()

            chain.proceed(request)
        }

        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideNetworkCheckService(application: Application): INetworkCheckService {
        return object : INetworkCheckService {
            override fun hasInternet(): Boolean {
                return Utils.hasInternet(application)
            }
        }
    }
}