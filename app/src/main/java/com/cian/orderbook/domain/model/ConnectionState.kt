package com.cian.orderbook.domain.model

// Transport-agnostic connection status surfaced to the rest of the app
sealed interface ConnectionState {
    data object Idle : ConnectionState
    data object Connecting : ConnectionState
    data object Connected : ConnectionState
    data class Reconnecting(val attempt: Int, val delayMs: Long) : ConnectionState
    data class Disconnected(val reason: String?) : ConnectionState
}
