package com.cian.orderbook.data.repository

import com.cian.orderbook.core.dispatcher.DispatcherProvider
import com.cian.orderbook.data.remote.bitget.BitgetEndpoints
import com.cian.orderbook.data.remote.bitget.BitgetWebSocketClient
import com.cian.orderbook.data.remote.bitget.aggregation.OrderBookAggregator
import com.cian.orderbook.data.remote.bitget.message.BitgetSocketMessage
import com.cian.orderbook.domain.model.ConnectionState
import com.cian.orderbook.domain.model.OrderBook
import com.cian.orderbook.domain.repository.OrderBookRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapNotNull

class OrderBookRepositoryImpl @Inject constructor(
    private val webSocketClient: BitgetWebSocketClient,
    private val aggregator: OrderBookAggregator,
    private val dispatcherProvider: DispatcherProvider
) : OrderBookRepository {

    override val connectionState: StateFlow<ConnectionState> = webSocketClient.connectionState

    override fun observeOrderBook(): Flow<OrderBook> = webSocketClient.messages
        .filterIsInstance<BitgetSocketMessage.OrderBookPush>()
        .mapNotNull { push -> aggregator.apply(push) }
        .flowOn(dispatcherProvider.default)

    override fun connect() {
        webSocketClient.connect(subscriptions = listOf(BitgetEndpoints.orderBookSubscription()))
    }

    override fun disconnect() {
        webSocketClient.disconnect()
        aggregator.reset()
    }
}
