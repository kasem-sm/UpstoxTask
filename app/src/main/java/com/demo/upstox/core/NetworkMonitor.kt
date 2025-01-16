package com.demo.upstox.core

import kotlinx.coroutines.flow.Flow

interface NetworkMonitor {
    val isConnected: Flow<Boolean>
}