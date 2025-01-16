package com.demo.upstox.data.remote.model.response

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.math.BigDecimal

@Serializable
data class HoldingsResponse(
    val data: Data
) {
    @Serializable
    data class Data(
        @SerialName("userHolding")
        val holdings: List<Holdings>
    )
}

@Serializable
data class Holdings(
    val symbol: String,
    val quantity: Int,
    @SerialName("ltp")
    val lastTradedPrice: Double,
    @SerialName("avgPrice")
    val averagePrice: Double,
    val close: Double,
)
