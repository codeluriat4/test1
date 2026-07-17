package com.cian.orderbook.data.remote.bitget.message

import com.cian.orderbook.data.remote.bitget.dto.BitgetEventResponseDto
import com.cian.orderbook.data.remote.bitget.dto.BitgetPushEnvelopeDto
import javax.inject.Inject
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

private const val PONG_TEXT = "pong"

class DefaultBitgetMessageParser @Inject constructor(
    private val json: Json
) : BitgetMessageParser {

    override fun parse(raw: String): BitgetSocketMessage? {
        if (raw == PONG_TEXT) return BitgetSocketMessage.Pong

        val element = runCatching { json.parseToJsonElement(raw) }.getOrNull() as? JsonObject ?: return null
        return when {
            "event" in element -> parseEvent(element)
            "action" in element -> parsePush(element)
            else -> null
        }
    }

    private fun parseEvent(element: JsonObject): BitgetSocketMessage {
        val dto = json.decodeFromJsonElement(BitgetEventResponseDto.serializer(), element)
        return if (dto.event == "subscribe" && dto.code == null) {
            BitgetSocketMessage.SubscriptionAcknowledged(
                instId = dto.arg?.instId.orEmpty(),
                channel = dto.arg?.channel.orEmpty()
            )
        } else {
            BitgetSocketMessage.SubscriptionFailed(
                code = dto.code ?: "unknown",
                reason = dto.msg ?: "subscription rejected"
            )
        }
    }

    private fun parsePush(element: JsonObject): BitgetSocketMessage? {
        val envelope = runCatching {
            json.decodeFromJsonElement(BitgetPushEnvelopeDto.serializer(), element)
        }.getOrNull() ?: return null

        val payload = envelope.data?.firstOrNull() ?: return null
        val action = when (envelope.action) {
            "snapshot" -> PushAction.SNAPSHOT
            "update" -> PushAction.UPDATE
            else -> return null
        }

        return BitgetSocketMessage.OrderBookPush(
            action = action,
            instId = envelope.arg.instId,
            payload = payload
        )
    }
}
