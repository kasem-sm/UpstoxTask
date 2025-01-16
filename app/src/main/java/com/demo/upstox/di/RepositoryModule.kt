package com.demo.upstox.di

import com.demo.upstox.data.repository.DefaultHoldingsRepository
import com.demo.upstox.data.repository.HoldingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindHoldingsRepository(
        repository: DefaultHoldingsRepository
    ): HoldingsRepository
}
