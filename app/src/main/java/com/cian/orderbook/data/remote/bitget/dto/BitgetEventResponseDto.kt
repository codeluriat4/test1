package com.cian.orderbook.data.remote.bitget.dto

import kotlinx.serialization.Serializable

// Inbound control frame acknowledging/rejecting a subscribe or unsubscribe request
@Serializable
data class BitgetEventResponseDto(
    val event: String,
    val arg: BitgetChannelArgDto? = null,
    val code: String? = null,
    val msg: String? = null
)
