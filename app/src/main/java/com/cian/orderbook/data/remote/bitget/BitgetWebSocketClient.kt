package com.cian.orderbook.data.remote.bitget

import com.cian.orderbook.data.remote.bitget.dto.BitgetChannelArgDto
import com.cian.orderbook.data.remote.bitget.message.BitgetSocketMessage
import com.cian.orderbook.domain.model.ConnectionState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

// Owns the physical WebSocket connection to Bitget: opening, closing, heartbeat and reconnect
interface BitgetWebSocketClient {

    val messages: Flow<BitgetSocketMessage>
    val connectionState: StateFlow<ConnectionState>

    // Connects and subscribes to the given channels; the same list is resent automatically on reconnect
    fun connect(subscriptions: List<BitgetChannelArgDto>)

    fun disconnect()
}
