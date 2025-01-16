package com.demo.upstox.di

import android.content.Context
import com.demo.upstox.core.AndroidNetworkMonitor
import com.demo.upstox.core.DispatchersProvider
import com.demo.upstox.core.NetworkMonitor
import com.demo.upstox.data.remote.AppNetworkService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClientBuilder(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClientBuilder: OkHttpClient.Builder,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(APP_BASE_URL)
            .client(okHttpClientBuilder.build())
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Provides
    @Singleton
    fun provideAppNetworkService(
        retrofit: Retrofit,
    ): AppNetworkService {
        return retrofit.create(AppNetworkService::class.java)
    }

    @Provides
    @Singleton
    fun provideNetworkMonitor(
        @ApplicationContext context: Context,
        dispatchersProvider: DispatchersProvider
    ): NetworkMonitor {
        return AndroidNetworkMonitor(context, dispatchersProvider)
    }

    private const val APP_BASE_URL = "https://35dee773a9ec441e9f38d5fc249406ce.api.mockbin.io/"
    private val json = Json {
        ignoreUnknownKeys = true
    }
}
