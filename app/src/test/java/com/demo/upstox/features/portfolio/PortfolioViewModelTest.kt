@file:OptIn(ExperimentalCoroutinesApi::class)

package com.demo.upstox.features.portfolio

import app.cash.turbine.test
import com.demo.upstox.core.DispatchersProvider
import com.demo.upstox.core.NetworkMonitor
import com.demo.upstox.data.local.model.HoldingEntity
import com.demo.upstox.data.repository.HoldingsRepository
import com.demo.upstox.domain.CalculatePortfolioSummary
import com.demo.upstox.domain.GetHoldingsWithPnLUseCase
import com.demo.upstox.domain.RefreshHoldingsUseCase
import com.demo.upstox.domain.testHoldings
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okio.IOException
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class PortfolioViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var dispatchers: DispatchersProvider

    private lateinit var repository: HoldingsRepository

    private lateinit var viewModel: PortfolioViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        dispatchers = object : DispatchersProvider {
            override val io: CoroutineDispatcher
                get() = testDispatcher
            override val default: CoroutineDispatcher
                get() = testDispatcher
        }
    }

    @Test
    fun verifyDataIsStaleWhenUserIsOffline() = runTest {
        repository = object : HoldingsRepository {
            override fun observeHoldings() = flowOf(testHoldings)
            override suspend fun refreshHoldings() =
                Result.failure<Unit>(IOException("No internet"))
        }

        viewModel = PortfolioViewModel(
            getHoldingsWithPnL = GetHoldingsWithPnLUseCase(repository, dispatchers),
            calculatePortfolioSummary = CalculatePortfolioSummary(repository, dispatchers),
            networkMonitor = object : NetworkMonitor {
                override val isConnected = flow { emit(false) }
            },
            refreshHoldingUseCase = RefreshHoldingsUseCase(repository)
        )

        viewModel.uiState.test {
            val firstEmission = awaitItem()
            assert(firstEmission is PortfolioUiState.Loading)

            val secondEmission = awaitItem()
            assert(secondEmission is PortfolioUiState.Success)
            assertEquals(true, (secondEmission as PortfolioUiState.Success).isDataStale)
        }
    }

    @Test
    fun verifyWhenUserIsOffline_And_CacheIsEmpty_Then_StateIsOffline() = runTest {
        repository = object : HoldingsRepository {
            override fun observeHoldings() = flowOf(emptyList<HoldingEntity>())
            override suspend fun refreshHoldings() =
                Result.failure<Unit>(IOException("No internet"))
        }

        viewModel = PortfolioViewModel(
            getHoldingsWithPnL = GetHoldingsWithPnLUseCase(repository, dispatchers),
            calculatePortfolioSummary = CalculatePortfolioSummary(repository, dispatchers),
            networkMonitor = object : NetworkMonitor {
                override val isConnected = flow { emit(false) }
            },
            refreshHoldingUseCase = RefreshHoldingsUseCase(repository)
        )

        viewModel.uiState.test {
            val firstEmission = awaitItem()
            assertEquals(PortfolioUiState.Loading, firstEmission)

            val secondEmission = awaitItem()
            assertEquals(PortfolioUiState.Error.Offline, secondEmission)
        }
    }

    @Test
    fun verifyWhenUserIsOffline_And_CacheIs_Not_Empty_Then_StateIsSuccess() = runTest {
        repository = object : HoldingsRepository {
            override fun observeHoldings() = flowOf(testHoldings)
            override suspend fun refreshHoldings() =
                Result.failure<Unit>(IOException("No internet"))
        }

        viewModel = PortfolioViewModel(
            getHoldingsWithPnL = GetHoldingsWithPnLUseCase(repository, dispatchers),
            calculatePortfolioSummary = CalculatePortfolioSummary(repository, dispatchers),
            networkMonitor = object : NetworkMonitor {
                override val isConnected = flow { emit(false) }
            },
            refreshHoldingUseCase = RefreshHoldingsUseCase(repository)
        )

        viewModel.uiState.test {
            val firstEmission = awaitItem()
            assertEquals(PortfolioUiState.Loading, firstEmission)

            val secondEmission = awaitItem()
            assert(secondEmission is PortfolioUiState.Success)
            assertEquals(true, (secondEmission as PortfolioUiState.Success).isDataStale)
        }
    }
}