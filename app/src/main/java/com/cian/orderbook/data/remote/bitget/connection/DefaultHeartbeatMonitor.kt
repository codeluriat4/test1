package com.cian.orderbook.data.remote.bitget.connection

import com.cian.orderbook.data.remote.bitget.BitgetEndpoints
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class DefaultHeartbeatMonitor @Inject constructor() : HeartbeatMonitor {

    private var pingJob: Job? = null
    private var timeoutJob: Job? = null

    override fun start(scope: CoroutineScope, sendPing: () -> Unit, onTimeout: () -> Unit) {
        stop()
        pingJob = scope.launch {
            while (isActive) {
                delay(BitgetEndpoints.PING_INTERVAL_MS)
                sendPing()
                armTimeout(scope, onTimeout)
            }
        }
    }

    private fun armTimeout(scope: CoroutineScope, onTimeout: () -> Unit) {
        timeoutJob?.cancel()
        timeoutJob = scope.launch {
            delay(BitgetEndpoints.PONG_TIMEOUT_MS)
            onTimeout()
        }
    }

    override fun acknowledgePong() {
        timeoutJob?.cancel()
    }

    override fun stop() {
        pingJob?.cancel()
        timeoutJob?.cancel()
        pingJob = null
        timeoutJob = null
    }
}
