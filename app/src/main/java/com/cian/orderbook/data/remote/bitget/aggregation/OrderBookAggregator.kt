package com.cian.orderbook.data.remote.bitget.aggregation

import com.cian.orderbook.data.remote.bitget.message.BitgetSocketMessage
import com.cian.orderbook.domain.model.OrderBook

// Maintains the local depth state and folds snapshot/update pushes into a single current OrderBook
interface OrderBookAggregator {

    // Returns null when an update arrives before any snapshot has been applied, or for a different instrument
    fun apply(push: BitgetSocketMessage.OrderBookPush): OrderBook?

    fun reset()
}
