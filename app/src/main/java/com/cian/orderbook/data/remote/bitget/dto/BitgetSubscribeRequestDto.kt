package com.cian.orderbook.data.remote.bitget.dto

import kotlinx.serialization.Serializable

// Outbound frame: {"op":"subscribe","args":[...]} (also reused for "unsubscribe")
@Serializable
data class BitgetSubscribeRequestDto(
    val op: String,
    val args: List<BitgetChannelArgDto>
)
