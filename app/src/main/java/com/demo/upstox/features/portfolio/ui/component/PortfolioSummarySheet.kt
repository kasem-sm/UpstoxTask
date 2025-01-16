package com.demo.upstox.features.portfolio.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.demo.upstox.domain.model.PortfolioSummary
import com.demo.upstox.features.portfolio.ui.util.getPnLColor
import com.demo.upstox.features.portfolio.ui.util.toStockPriceFormat
import java.math.BigDecimal

@Composable
fun PortfolioSummarySheet(
    model: PortfolioSummary,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(MaterialTheme.colorScheme.primaryContainer.copy(.5f))
            .padding(10.dp)
    ) {
        PortfolioSummarySheetContent(model)
    }
}

@Composable
private fun PortfolioSummarySheetContent(
    model: PortfolioSummary,
    modifier: Modifier = Modifier,
) {
    val localHapticFeedback = LocalHapticFeedback.current
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .padding(12.dp),
    ) {
        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            // Content
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                SummaryItem(title = "Current value*", value = model.currentValue)
                SummaryItem(title = "Total investment*", value = model.investedValue)
                SummaryItem(
                    title = "Today's Profit & Loss*",
                    value = model.todayPnL,
                    color = model.isTodayProfit.getPnLColor(profitColor = MaterialTheme.colorScheme.primary),
                )
                HorizontalDivider()

                Spacer(modifier = Modifier.height(10.dp))
            }
        }

        // Footer
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.medium)
                .clickable {
                    localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                    isExpanded = !isExpanded
                }
                .padding(6.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Profit & Loss*",
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowDown
                    else Icons.Default.KeyboardArrowUp,
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Text(
                text = model.totalPnL.toStockPriceFormat(),
                color = model.isTotalProfit.getPnLColor(profitColor = MaterialTheme.colorScheme.primary),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun SummaryItem(
    modifier: Modifier = Modifier,
    title: String,
    value: BigDecimal,
    color: Color = LocalContentColor.current
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title, fontSize = 20.sp, color = MaterialTheme.colorScheme.onPrimaryContainer)

        Text(value.toStockPriceFormat(), fontSize = 20.sp, color = color)
    }
}
