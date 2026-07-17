package com.cian.orderbook.data.remote.bitget.message

// Turns a raw WebSocket text frame into a typed message, or null if it can't be understood
interface BitgetMessageParser {
    fun parse(raw: String): BitgetSocketMessage?
}
