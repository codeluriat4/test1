package com.cian.orderbook.domain.repository

import com.cian.orderbook.domain.model.ConnectionState
import com.cian.orderbook.domain.model.OrderBook
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

// Single entry point the UI layer depends on; hides Bitget transport details entirely
interface OrderBookRepository {

    val connectionState: StateFlow<ConnectionState>

    // Cold, distinct stream of merged order book state; starts emitting once a snapshot arrives
    fun observeOrderBook(): Flow<OrderBook>

    fun connect()

    fun disconnect()
}
