package com.cian.orderbook.data.remote.bitget.connection

import kotlinx.coroutines.CoroutineScope

// Owns the ping/pong liveness check for one connection; the socket itself stays unaware of timing
interface HeartbeatMonitor {

    // Starts the periodic ping loop. sendPing is invoked on schedule, onTimeout when a pong doesn't arrive in time
    fun start(scope: CoroutineScope, sendPing: () -> Unit, onTimeout: () -> Unit)

    // Call when a "pong" frame is received to cancel the pending timeout
    fun acknowledgePong()

    fun stop()
}
