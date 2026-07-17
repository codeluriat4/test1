package com.cian.orderbook.data.remote.bitget.connection

// Decides how long to wait before the next reconnect attempt; swappable independent of the socket client
interface ReconnectPolicy {
    fun nextDelayMillis(attempt: Int): Long
}
