package com.demo.upstox.domain.model

import java.math.BigDecimal

data class PortfolioSummary(
    val currentValue: BigDecimal,
    val investedValue: BigDecimal,
    val todayPnL: BigDecimal,
    val totalPnL: BigDecimal,
    val isTotalProfit: Boolean = totalPnL > BigDecimal.ZERO,
    val isTodayProfit: Boolean = todayPnL > BigDecimal.ZERO,
)