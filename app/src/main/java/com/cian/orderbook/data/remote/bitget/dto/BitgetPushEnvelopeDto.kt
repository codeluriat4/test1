package com.cian.orderbook.data.remote.bitget.dto

import kotlinx.serialization.Serializable

// Inbound market-data frame: action is "snapshot" on first push, "update" for incremental diffs
@Serializable
data class BitgetPushEnvelopeDto(
    val action: String? = null,
    val arg: BitgetChannelArgDto,
    val data: List<OrderBookPayloadDto>? = null,
    val ts: Long? = null
)
