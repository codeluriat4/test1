package com.cian.orderbook.data.remote.bitget

import com.cian.orderbook.data.remote.bitget.dto.BitgetChannelArgDto

// Single source of truth for Bitget public WS connection parameters and channel construction
object BitgetEndpoints {

    const val PUBLIC_WS_URL = "wss://ws.bitget.com/v2/ws/public"

    const val INST_TYPE_USDT_FUTURES = "USDT-FUTURES"
    const val CHANNEL_ORDER_BOOK = "books"
    const val DEFAULT_SYMBOL = "BTCUSDT"

    // Server disconnects if no "ping" is seen for 2 minutes; 30s keeps well inside that budget
    const val PING_INTERVAL_MS = 30_000L
    const val PONG_TIMEOUT_MS = 10_000L

    fun orderBookSubscription(symbol: String = DEFAULT_SYMBOL): BitgetChannelArgDto = BitgetChannelArgDto(
        instType = INST_TYPE_USDT_FUTURES,
        channel = CHANNEL_ORDER_BOOK,
        instId = symbol
    )
}
