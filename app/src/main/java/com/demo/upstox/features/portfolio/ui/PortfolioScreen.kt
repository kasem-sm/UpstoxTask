package com.demo.upstox.features.portfolio.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.demo.upstox.core.ui.NavigationBarColor
import com.demo.upstox.features.portfolio.PortfolioUiState
import com.demo.upstox.features.portfolio.PortfolioViewModel
import com.demo.upstox.features.portfolio.ui.component.HoldingsList
import com.demo.upstox.features.portfolio.ui.component.PortfolioSummarySheet
import com.demo.upstox.features.portfolio.ui.component.StaleDataBanner

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PortfolioScreen(
    modifier: Modifier = Modifier,
    viewModel: PortfolioViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    NavigationBarColor(
        uiState,
        color = if (uiState is PortfolioUiState.Success) {
            MaterialTheme.colorScheme.primaryContainer.copy(.5f)
        } else {
            MaterialTheme.colorScheme.surface
        }
    )

    PullToRefreshBox(
        isRefreshing = uiState is PortfolioUiState.Loading,
        onRefresh = viewModel::refresh,
        modifier = modifier
    ) {
        PortfolioScreenContent(
            state = uiState,
            onRefresh = viewModel::refresh
        )
    }
}

@Composable
private fun PortfolioScreenContent(
    state: PortfolioUiState,
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        when (state) {
            PortfolioUiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            is PortfolioUiState.Success -> {
                Column(Modifier.fillMaxSize()) {
                    if (state.isDataStale) {
                        StaleDataBanner()
                    }

                    HoldingsList(
                        holdings = state.holdings,
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                    )

                    state.portfolioSummary?.let {
                        PortfolioSummarySheet(it)
                    }
                }
            }

            is PortfolioUiState.Error -> PortfolioErrorBody(
                state = state,
                onRefresh = onRefresh
            )
        }
    }
}

@Composable
private fun PortfolioErrorBody(
    state: PortfolioUiState.Error,
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (state) {
            is PortfolioUiState.Error.Unknown -> {
                Text(
                    text = state.message,
                )
            }

            is PortfolioUiState.Error.Offline -> {
                Text(
                    text = "Please check your internet connection",
                )

                Spacer(modifier = Modifier.height(10.dp))

                Button(onClick = onRefresh) {
                    Text("Refresh")
                }
            }
        }
    }
}
