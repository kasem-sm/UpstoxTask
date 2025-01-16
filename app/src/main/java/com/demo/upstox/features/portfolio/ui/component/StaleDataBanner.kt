package com.demo.upstox.features.portfolio.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StaleDataBanner(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.primaryContainer.copy(.5f))
            .padding(10.dp)
            .fillMaxWidth(),
    ) {
        Text(text = "Data may be outdated. Please check your internet connection.",)
    }
}
