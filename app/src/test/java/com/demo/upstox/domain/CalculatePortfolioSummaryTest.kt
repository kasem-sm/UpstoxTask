package com.demo.upstox.domain

import app.cash.turbine.test
import com.demo.upstox.core.DispatchersProvider
import com.demo.upstox.data.local.model.HoldingEntity
import com.demo.upstox.data.repository.HoldingsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal

class CalculatePortfolioSummaryTest {

    private lateinit var dispatchers: DispatchersProvider
    private lateinit var useCase: CalculatePortfolioSummary
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        dispatchers = object : DispatchersProvider {
            override val io: CoroutineDispatcher
                get() = testDispatcher
            override val default: CoroutineDispatcher
                get() = testDispatcher
        }
    }

    @Test
    fun totalInvestment() = runTest(testDispatcher) {
        val fake = object : HoldingsRepository {
            override fun observeHoldings() = flowOf(testHoldings)
            override suspend fun refreshHoldings() = Result.success(Unit)
        }

        useCase = CalculatePortfolioSummary(fake, dispatchers)
        val expectedTotalInvestment = BigDecimal("337563.75")

        useCase.execute().test {
            val firstEmission = awaitItem()
            assertEquals(expectedTotalInvestment, firstEmission.investedValue)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun testPortfolioRealtimeUpdatesWhenHoldingChanges() = runTest(testDispatcher) {
        val mockFlow = MutableStateFlow(testHoldings)

        val fake = object : HoldingsRepository {
            override fun observeHoldings() = mockFlow
            override suspend fun refreshHoldings() = Result.success(Unit)
        }

        useCase = CalculatePortfolioSummary(fake, dispatchers)
        val expectedTotalInvestment = BigDecimal("337563.75")

        useCase.execute().test {
            val firstEmitted = awaitItem()
            assertEquals(expectedTotalInvestment, firstEmitted.investedValue)

            mockFlow.emit(
                testHoldings + HoldingEntity(
                    symbol = "WIPRO",
                    quantity = 10,
                    averagePrice = BigDecimal("500.00"),
                    lastTradedPrice = BigDecimal("480.00"),
                    close = BigDecimal("520.00")
                )
            )

            // expectedTotalInvestment should increase by (500 * 10) = 5000
            val newExpectedTotalInvestment = BigDecimal("342563.75")
            val secondEmitted = awaitItem()
            assertEquals(newExpectedTotalInvestment, secondEmitted.investedValue)
        }
    }
}
