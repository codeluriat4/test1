package com.cian.orderbook.domain.model

// Immutable snapshot of the merged local order book at a point in time, sorted best-first on both sides
data class OrderBook(
    val instrumentId: String,
    val bids: List<PriceLevel>,
    val asks: List<PriceLevel>,
    val sequence: Long,
    val timestampMs: Long
)
