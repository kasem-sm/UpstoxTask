package com.demo.upstox.di

import android.content.Context
import androidx.room.Room
import com.demo.upstox.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "demo_upstox.db",
        ).build()
    }

    @Provides
    @Singleton
    fun provideHoldingsDao(
        appDatabase: AppDatabase
    ) = appDatabase.holdingsDao()
}
