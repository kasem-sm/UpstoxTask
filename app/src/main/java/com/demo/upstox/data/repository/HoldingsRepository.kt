package com.demo.upstox.data.repository

import com.demo.upstox.data.local.model.HoldingEntity
import kotlinx.coroutines.flow.Flow

interface HoldingsRepository {
    fun observeHoldings(): Flow<List<HoldingEntity>>
    suspend fun refreshHoldings(): Result<Unit>
}