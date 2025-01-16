package com.demo.upstox.domain

import com.demo.upstox.core.DispatchersProvider
import com.demo.upstox.data.local.model.HoldingEntity
import com.demo.upstox.data.repository.HoldingsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.io.IOException

class RefreshHoldingsUseCaseTest {

    private lateinit var repository: HoldingsRepository
    private lateinit var dispatchers: DispatchersProvider
    private lateinit var useCase: RefreshHoldingsUseCase
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
    fun verifyExecuteReturnsOfflineWhen_IOExceptionIsReturned() = runTest(testDispatcher) {
        repository = object : HoldingsRepository {
            override fun observeHoldings() = emptyFlow<List<HoldingEntity>>()
            override suspend fun refreshHoldings() = Result.failure<Unit>(IOException())
        }

        useCase = RefreshHoldingsUseCase(repository)
        val result = useCase.execute()
        assert(result is RefreshResult.Offline)
    }

    @Test
    fun verifyExecuteReturnsErrorWhen_NonIOExceptionIsReturned() = runTest(testDispatcher) {
        repository = object : HoldingsRepository {
            override fun observeHoldings() = emptyFlow<List<HoldingEntity>>()
            override suspend fun refreshHoldings() = Result.failure<Unit>(Exception())
        }

        useCase = RefreshHoldingsUseCase(repository)
        val result = useCase.execute()
        assert(result is RefreshResult.Error)
    }
}
