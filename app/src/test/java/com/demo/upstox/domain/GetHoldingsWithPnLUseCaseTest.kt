package com.demo.upstox.domain

import app.cash.turbine.test
import com.demo.upstox.core.DispatchersProvider
import com.demo.upstox.data.local.model.HoldingEntity
import com.demo.upstox.data.repository.HoldingsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal

private class FakeHoldingsRepository(
    private val holdings: List<HoldingEntity>
) : HoldingsRepository {
    override fun observeHoldings() = flowOf(holdings)
    override suspend fun refreshHoldings() = Result.success(Unit)
}

class GetHoldingsWithPnLUseCaseTest {

    private lateinit var dispatchers: DispatchersProvider
    private lateinit var useCase: GetHoldingsWithPnLUseCase
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
    fun verifyPnLFor_Reliance() = runTest(testDispatcher) {
        val fakeRepository = FakeHoldingsRepository(
            listOf(
                HoldingEntity(
                    symbol = "RELIANCE",
                    quantity = 50,
                    averagePrice = BigDecimal("2450.75"),
                    lastTradedPrice = BigDecimal("2488.30"),
                    close = BigDecimal("2475.15")
                )
            )
        )
        useCase = GetHoldingsWithPnLUseCase(fakeRepository, dispatchers)

        useCase.execute().test {
            val result = awaitItem().first()

            val expectedPnL = BigDecimal("1877.50")

            assertEquals(expectedPnL, result.pnl)
            assertEquals(true, result.isProfit)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun verifyPnLFor_TCS() = runTest(testDispatcher) {
        val fakeRepository = FakeHoldingsRepository(
            listOf(
                HoldingEntity(
                    symbol = "TCS",
                    quantity = 25,
                    averagePrice = BigDecimal("3575.25"),
                    lastTradedPrice = BigDecimal("3575.25"),
                    close = BigDecimal("3562.40")
                ),
            )
        )
        useCase = GetHoldingsWithPnLUseCase(fakeRepository, dispatchers)
        val result = useCase.execute().first()[0]

        val totalPnl = BigDecimal("0.00")
        assertEquals(totalPnl, result.pnl)
        assertEquals(false, result.isProfit)
    }

    @Test
    fun whenQuantityIsZero_Then_PnL_IsZero() = runTest(testDispatcher) {
        val fakeRepository = FakeHoldingsRepository(
            listOf(
                HoldingEntity(
                    symbol = "AMBUJACEM",
                    quantity = 0,
                    averagePrice = BigDecimal("1.22"),
                    lastTradedPrice = BigDecimal("3575.25"),
                    close = BigDecimal("3562.40")
                ),
            )
        )
        useCase = GetHoldingsWithPnLUseCase(fakeRepository, dispatchers)
        useCase.execute().test {
            val result = awaitItem().first()

            assertEquals(BigDecimal.ZERO.setScale(2), result.pnl)
            assertEquals(false, result.isProfit)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun checkProfit_IsFalse_For_NegativePnL() = runTest(testDispatcher) {
        val fakeRepository = FakeHoldingsRepository(
            listOf(
                HoldingEntity(
                    symbol = "INFY",
                    quantity = 1,
                    averagePrice = BigDecimal("1.25"),
                    lastTradedPrice = BigDecimal("1.00"),
                    close = BigDecimal("1.00")
                )
            )
        )
        useCase = GetHoldingsWithPnLUseCase(fakeRepository, dispatchers)

        useCase.execute().test {
            val result = awaitItem().first()

            assertEquals(false, result.isProfit)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
