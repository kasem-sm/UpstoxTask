package com.demo.upstox.domain

import com.demo.upstox.core.DispatchersProvider
import com.demo.upstox.data.repository.HoldingsRepository
import com.demo.upstox.domain.model.PortfolioSummary
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CalculatePortfolioSummary @Inject constructor(
    private val repository: HoldingsRepository,
    private val dispatchersProvider: DispatchersProvider
) {
    fun execute(): Flow<PortfolioSummary> {
        return repository.observeHoldings()
            .distinctUntilChanged()
            .map { allHoldings ->
                val totalInvestment = allHoldings.sumOf {
                    it.averagePrice.multiply(it.quantity.toBigDecimal())
                }
                val currentValue = allHoldings.sumOf {
                    it.lastTradedPrice.multiply(it.quantity.toBigDecimal())
                }
                val totalPnL = currentValue - totalInvestment
                val todayPnL = allHoldings.sumOf {
                    (it.close - it.lastTradedPrice).multiply(it.quantity.toBigDecimal())
                }
                PortfolioSummary(
                    investedValue = totalInvestment,
                    currentValue = currentValue,
                    totalPnL = totalPnL,
                    todayPnL = todayPnL
                )
            }.flowOn(dispatchersProvider.default)
    }
}