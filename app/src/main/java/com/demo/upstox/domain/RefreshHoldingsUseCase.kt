package com.demo.upstox.domain

import com.demo.upstox.data.repository.HoldingsRepository
import java.io.IOException
import javax.inject.Inject

class RefreshHoldingsUseCase @Inject constructor(
    private val repository: HoldingsRepository,
) {
    suspend fun execute(): RefreshResult {
        val result = repository.refreshHoldings()
        return when {
            result.isSuccess -> RefreshResult.Success
            result.exceptionOrNull() is IOException -> RefreshResult.Offline
            else -> RefreshResult.Error("Failed to refresh")
        }
    }
}

sealed interface RefreshResult {
    data object Offline : RefreshResult // to show message in UI that data may be stale
    data object Success : RefreshResult
    data class Error(val message: String) : RefreshResult
}
