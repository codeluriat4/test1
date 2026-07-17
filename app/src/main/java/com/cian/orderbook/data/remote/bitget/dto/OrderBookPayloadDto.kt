package com.cian.orderbook.data.remote.bitget.dto

import kotlinx.serialization.Serializable

// Raw depth payload; asks/bids are [price, quantity] string pairs, quantity 0 means "remove this level"
@Serializable
data class OrderBookPayloadDto(
    val asks: List<List<String>>,
    val bids: List<List<String>>,
    val checksum: Long,
    val seq: Long,
    val ts: String
)
