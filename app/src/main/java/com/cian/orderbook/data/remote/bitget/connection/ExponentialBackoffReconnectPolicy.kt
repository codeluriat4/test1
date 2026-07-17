package com.cian.orderbook.data.remote.bitget.connection

import javax.inject.Inject
import kotlin.math.pow
import kotlin.random.Random

class ExponentialBackoffReconnectPolicy @Inject constructor() : ReconnectPolicy {

    private val initialDelayMs = 1_000L
    private val maxDelayMs = 30_000L
    private val multiplier = 2.0
    private val jitterBoundMs = 250L

    override fun nextDelayMillis(attempt: Int): Long {
        val exponential = initialDelayMs * multiplier.pow(attempt - 1).toLong()
        val jitter = Random.nextLong(jitterBoundMs)
        return exponential.coerceAtMost(maxDelayMs) + jitter
    }
}
