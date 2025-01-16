package com.demo.upstox.data.repository

import android.util.Log
import com.demo.upstox.core.DispatchersProvider
import com.demo.upstox.data.local.dao.HoldingsDao
import com.demo.upstox.data.local.model.HoldingEntity
import com.demo.upstox.data.remote.AppNetworkService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultHoldingsRepository @Inject constructor(
    private val network: AppNetworkService,
    private val holdingsDao: HoldingsDao,
    private val dispatchersProvider: DispatchersProvider,
) : HoldingsRepository {

    override suspend fun refreshHoldings(): Result<Unit> {
        return withContext(dispatchersProvider.io) {
            try {
                val response = network.getHoldings()
                holdingsDao.upsertHoldings(
                    response.data.holdings.map {
                        HoldingEntity(
                            symbol = it.symbol,
                            quantity = it.quantity,
                            lastTradedPrice = it.lastTradedPrice.toBigDecimal(),
                            averagePrice = it.averagePrice.toBigDecimal(),
                            close = it.close.toBigDecimal()
                        )
                    })
                Result.success(Unit)
            } catch (e: Exception) {
                Log.e("DefaultHoldingsRepository", "refreshHoldings: failed", e)
                Result.failure(e)
            }
        }
    }

    override fun observeHoldings(): Flow<List<HoldingEntity>> {
        return holdingsDao.getHoldings()
    }
}
