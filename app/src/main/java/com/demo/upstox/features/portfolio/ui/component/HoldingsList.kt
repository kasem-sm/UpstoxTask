package com.demo.upstox.features.portfolio.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.demo.upstox.R
import com.demo.upstox.domain.model.HoldingWithPnL
import com.demo.upstox.features.portfolio.ui.util.getPnLColor
import com.demo.upstox.features.portfolio.ui.util.toStockPriceFormat

@Composable
fun HoldingsList(
    holdings: List<HoldingWithPnL>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
    ) {
        itemsIndexed(holdings) { index, item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 12.dp,
                        bottom = 12.dp,
                        start = 20.dp,
                        end = 20.dp
                    ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier.weight(.5f),
                    verticalArrangement = Arrangement.spacedBy(30.dp),
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = item.holding.symbol,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )

                        if (!item.isProfit) {
                            Icon(
                                painter = painterResource(R.drawable.ic_down),
                                tint = MaterialTheme.colorScheme.error,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                            ) {
                                append("NET QTY: ")
                            }
                            withStyle(
                                style = SpanStyle(
                                    fontSize = 20.sp,
                                ),
                            ) {
                                append("${item.holding.quantity}")
                            }
                        },
                    )
                }

                Column(
                    modifier = Modifier.weight(.5f),
                    verticalArrangement = Arrangement.spacedBy(30.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                            ) {
                                append("LTP: ")
                            }
                            withStyle(
                                style = SpanStyle(
                                    fontSize = 20.sp,
                                ),
                            ) {
                                append(item.holding.lastTradedPrice.toStockPriceFormat())
                            }
                        },
                        textAlign = TextAlign.End
                    )

                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                            ) {
                                append("P/L ")
                            }
                            withStyle(
                                style = SpanStyle(
                                    fontSize = 20.sp,
                                    color = item.isProfit.getPnLColor(),
                                    fontWeight = FontWeight.W500
                                ),
                            ) {
                                append(item.pnl.toStockPriceFormat())
                            }
                        },
                        textAlign = TextAlign.End
                    )
                }
            }

            if (index < holdings.size - 1) {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = MaterialTheme.colorScheme.surfaceTint.copy(alpha = 0.1f),
                )
            }
        }
    }
}
