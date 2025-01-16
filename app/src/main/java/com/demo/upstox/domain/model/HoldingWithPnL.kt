package com.demo.upstox.domain.model

import com.demo.upstox.data.local.model.HoldingEntity
import java.math.BigDecimal

data class HoldingWithPnL(
    val holding: HoldingEntity,
    val pnl: BigDecimal,
    val isProfit: Boolean = pnl > BigDecimal.ZERO
)
