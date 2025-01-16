package com.demo.upstox.core

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface DispatchersProvider {
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
}

class DefaultDispatchersProvider : DispatchersProvider {
    // During unit test, Android doc suggest to replace this with StandardTestDispatcher
    override val io = Dispatchers.IO
    override val default = Dispatchers.Default
}
