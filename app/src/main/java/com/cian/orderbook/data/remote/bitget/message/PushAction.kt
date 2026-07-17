package com.cian.orderbook.data.remote.bitget.message

// Mirrors Bitget's "action" field for depth pushes
enum class PushAction {
    SNAPSHOT,
    UPDATE
}
