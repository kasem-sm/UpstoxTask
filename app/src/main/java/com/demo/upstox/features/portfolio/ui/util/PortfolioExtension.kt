package com.demo.upstox.features.portfolio.ui.util

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat

fun BigDecimal.toStockPriceFormat(): String {
    val formatter = DecimalFormat("##,##,###.##").apply {
        // removes decimal places if it is 0 - eg - 7,899
        minimumFractionDigits = 0
        // shows up-to 2 decimal places if it is not 0 - eg - 7,899.50
        maximumFractionDigits = 2
        roundingMode = RoundingMode.HALF_UP // 2.5 rounds to 3 and 2.4 rounds to 2
    }
    return "₹ ${formatter.format(this)}"
}

@Composable
fun Boolean.getPnLColor(profitColor: Color = Color(0xFF12B07A)): Color {
    return if (this) profitColor else MaterialTheme.colorScheme.error
}
