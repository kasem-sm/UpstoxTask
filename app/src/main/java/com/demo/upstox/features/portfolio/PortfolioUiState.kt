package com.demo.upstox.features.portfolio

import com.demo.upstox.domain.model.HoldingWithPnL
import com.demo.upstox.domain.model.PortfolioSummary

sealed class PortfolioUiState {
    data object Loading : PortfolioUiState()
    data class Success(
        val holdings: List<HoldingWithPnL> = emptyList(),
        val portfolioSummary: PortfolioSummary? = null,
        val isDataStale: Boolean = false
    ) : PortfolioUiState()
    sealed class Error : PortfolioUiState() {
        data object Offline : Error()
        data class Unknown(val message: String) : Error()
    }
}
