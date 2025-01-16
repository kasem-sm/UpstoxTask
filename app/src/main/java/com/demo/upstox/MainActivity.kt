@file:OptIn(ExperimentalMaterial3Api::class)

package com.demo.upstox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.demo.upstox.features.portfolio.ui.PortfolioScreen
import com.demo.upstox.ui.theme.DemoUpstoxTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            DemoUpstoxTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(text = "Portfolio")
                            },
                            navigationIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.icon),
                                    contentDescription = "Icon",
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                )
                            }
                        )
                    }
                ) {
                    PortfolioScreen(
                        modifier = Modifier.padding(it)
                    )
                }
            }
        }
    }
}
