package com.cian.orderbook.data.remote.bitget.message

import com.cian.orderbook.data.remote.bitget.dto.OrderBookPayloadDto

// Parsed, typed representation of every frame the client can receive; replaces raw string branching downstream
sealed interface BitgetSocketMessage {

    data class OrderBookPush(
        val action: PushAction,
        val instId: String,
        val payload: OrderBookPayloadDto
    ) : BitgetSocketMessage

    data class SubscriptionAcknowledged(val instId: String, val channel: String) : BitgetSocketMessage

    data class SubscriptionFailed(val code: String, val reason: String) : BitgetSocketMessage

    data object Pong : BitgetSocketMessage
}
