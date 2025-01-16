package com.demo.upstox.di

import com.demo.upstox.core.DefaultDispatchersProvider
import com.demo.upstox.core.DispatchersProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DispatchersModule {

    @Provides
    @Singleton
    fun provideDefaultDispatcher(): DispatchersProvider {
        return DefaultDispatchersProvider()
    }
}