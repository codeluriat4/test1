package com.cian.orderbook.core.dispatcher

import kotlinx.coroutines.CoroutineDispatcher

// Abstraction over coroutine dispatchers so call sites depend on an interface, not a build-time branch
interface DispatcherProvider {
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
}
