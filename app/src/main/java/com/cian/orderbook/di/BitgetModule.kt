package com.cian.orderbook.di

import com.cian.orderbook.data.remote.bitget.BitgetWebSocketClient
import com.cian.orderbook.data.remote.bitget.BitgetWebSocketClientImpl
import com.cian.orderbook.data.remote.bitget.aggregation.DefaultOrderBookAggregator
import com.cian.orderbook.data.remote.bitget.aggregation.OrderBookAggregator
import com.cian.orderbook.data.remote.bitget.connection.DefaultHeartbeatMonitor
import com.cian.orderbook.data.remote.bitget.connection.ExponentialBackoffReconnectPolicy
import com.cian.orderbook.data.remote.bitget.connection.HeartbeatMonitor
import com.cian.orderbook.data.remote.bitget.connection.ReconnectPolicy
import com.cian.orderbook.data.remote.bitget.message.BitgetMessageParser
import com.cian.orderbook.data.remote.bitget.message.DefaultBitgetMessageParser
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BitgetModule {

    @Binds
    @Singleton
    abstract fun bindBitgetWebSocketClient(impl: BitgetWebSocketClientImpl): BitgetWebSocketClient

    @Binds
    abstract fun bindBitgetMessageParser(impl: DefaultBitgetMessageParser): BitgetMessageParser

    @Binds
    @Singleton
    abstract fun bindHeartbeatMonitor(impl: DefaultHeartbeatMonitor): HeartbeatMonitor

    @Binds
    abstract fun bindReconnectPolicy(impl: ExponentialBackoffReconnectPolicy): ReconnectPolicy

    @Binds
    @Singleton
    abstract fun bindOrderBookAggregator(impl: DefaultOrderBookAggregator): OrderBookAggregator
}
