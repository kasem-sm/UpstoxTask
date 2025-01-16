package com.demo.upstox.domain

import com.demo.upstox.core.DispatchersProvider
import com.demo.upstox.data.local.model.HoldingEntity
import com.demo.upstox.data.repository.HoldingsRepository
import com.demo.upstox.domain.model.HoldingWithPnL
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetHoldingsWithPnLUseCase @Inject constructor(
    private val repository: HoldingsRepository,
    private val dispatchersProvider: DispatchersProvider
) {
    fun execute(): Flow<List<HoldingWithPnL>> {
        return repository.observeHoldings()
            .map { holdings ->
                holdings.map { calculateIndividualPnL(it) }
            }
            .flowOn(dispatchersProvider.default)
    }

    private fun calculateIndividualPnL(holding: HoldingEntity): HoldingWithPnL {
        // the user purchased at this price
        val investedValue = holding.averagePrice.multiply(holding.quantity.toBigDecimal())
        // the current value of the holding
        val currentValue = holding.lastTradedPrice.multiply(holding.quantity.toBigDecimal())
        val totalPnL = currentValue - investedValue
        return HoldingWithPnL(holding, totalPnL)
    }
}
