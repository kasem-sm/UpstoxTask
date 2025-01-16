package com.demo.upstox.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(tableName = "holdings")
data class HoldingEntity(
    @PrimaryKey
    val symbol: String,
    val quantity: Int,
    val averagePrice: BigDecimal,
    val lastTradedPrice: BigDecimal,
    val close: BigDecimal,
)
