package com.demo.upstox.features.portfolio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.upstox.core.NetworkMonitor
import com.demo.upstox.domain.CalculatePortfolioSummary
import com.demo.upstox.domain.GetHoldingsWithPnLUseCase
import com.demo.upstox.domain.RefreshHoldingsUseCase
import com.demo.upstox.domain.RefreshResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PortfolioViewModel @Inject constructor(
    getHoldingsWithPnL: GetHoldingsWithPnLUseCase,
    calculatePortfolioSummary: CalculatePortfolioSummary,
    networkMonitor: NetworkMonitor,
    private val refreshHoldingUseCase: RefreshHoldingsUseCase,
) : ViewModel() {

    private val refreshState = MutableStateFlow<RefreshResult?>(null)

    private val isNetworkOnline = networkMonitor
        .isConnected
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    val uiState = combine(
        getHoldingsWithPnL.execute(),
        calculatePortfolioSummary.execute(),
        refreshState,
        isNetworkOnline
    ) { holdings, summary, refreshResult, isNetworkOnline ->
        when (refreshResult) {
            is RefreshResult.Error -> PortfolioUiState.Error.Unknown(refreshResult.message)
            RefreshResult.Offline -> {
                if (holdings.isEmpty()) PortfolioUiState.Error.Offline
                // cache data is available
                else PortfolioUiState.Success(holdings, summary, isDataStale = true)
            }

            RefreshResult.Success -> {
                // PS: the result can be success but still isDataStale can be true if network gets disconnected
                PortfolioUiState.Success(holdings, summary, isDataStale = isNetworkOnline.not())
            }

            null -> PortfolioUiState.Loading
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = PortfolioUiState.Loading
    )

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            refreshState.update { null }
            val result = refreshHoldingUseCase.execute()
            refreshState.update { result }
        }
    }
}