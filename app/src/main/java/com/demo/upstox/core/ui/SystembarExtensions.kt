package com.demo.upstox.core.ui

import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext

@Composable
fun NavigationBarColor(
    vararg keys: Any,
    color: Color = MaterialTheme.colorScheme.surface,
) {
    val context = LocalContext.current as ComponentActivity
    LaunchedEffect(*keys) {
        context.enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.dark(color.toArgb())
        )
    }
}
