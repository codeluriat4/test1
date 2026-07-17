package com.cian.orderbook.data.remote.bitget.dto

import kotlinx.serialization.Serializable

// Identifies a single channel subscription: product type + channel name + instrument
@Serializable
data class BitgetChannelArgDto(
    val instType: String,
    val channel: String,
    val instId: String
)
