package com.demo.upstox.domain

import com.demo.upstox.data.local.model.HoldingEntity
import java.math.BigDecimal

// (2450.75 x 50) + (1675.25 x 75) + (3575.25 x 25) + (1.22 x 0) + (1.25 x 1) = 337563.75
val testHoldings = listOf(
    HoldingEntity(
        symbol = "RELIANCE",
        quantity = 50,
        averagePrice = BigDecimal("2450.75"),
        lastTradedPrice = BigDecimal("2488.30"),
        close = BigDecimal("2475.15")
    ),
    HoldingEntity(
        symbol = "HDFCBANK",
        quantity = 75,
        averagePrice = BigDecimal("1675.25"),
        lastTradedPrice = BigDecimal("1698.45"),
        close = BigDecimal("1685.90")
    ),
    HoldingEntity(
        symbol = "TCS",
        quantity = 25,
        averagePrice = BigDecimal("3575.25"),
        lastTradedPrice = BigDecimal("3575.25"),
        close = BigDecimal("3562.40")
    ),
    HoldingEntity(
        symbol = "AMBUJACEM",
        quantity = 0,
        averagePrice = BigDecimal("1.22"),
        lastTradedPrice = BigDecimal("3575.25"),
        close = BigDecimal("3562.40")
    ),
    HoldingEntity(
        symbol = "INFY",
        quantity = 1,
        averagePrice = BigDecimal("1.25"),
        lastTradedPrice = BigDecimal("1.00"),
        close = BigDecimal("1.00")
    )
)