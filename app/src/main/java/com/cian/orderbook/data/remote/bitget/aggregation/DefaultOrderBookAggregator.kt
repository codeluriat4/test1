package com.cian.orderbook.data.remote.bitget.aggregation

import com.cian.orderbook.data.remote.bitget.dto.OrderBookPayloadDto
import com.cian.orderbook.data.remote.bitget.message.BitgetSocketMessage
import com.cian.orderbook.data.remote.bitget.message.PushAction
import com.cian.orderbook.domain.model.OrderBook
import com.cian.orderbook.domain.model.PriceLevel
import java.math.BigDecimal
import java.util.TreeMap
import javax.inject.Inject

class DefaultOrderBookAggregator @Inject constructor() : OrderBookAggregator {

    // Bids sorted highest-first, asks sorted lowest-first, matching how each side should be displayed
    private val bids = TreeMap<BigDecimal, BigDecimal>(Comparator.reverseOrder())
    private val asks = TreeMap<BigDecimal, BigDecimal>()

    private var instrumentId: String = ""
    private var hasSnapshot = false

    override fun apply(push: BitgetSocketMessage.OrderBookPush): OrderBook? = when (push.action) {
        PushAction.SNAPSHOT -> applySnapshot(push)
        PushAction.UPDATE -> applyUpdate(push)
    }

    private fun applySnapshot(push: BitgetSocketMessage.OrderBookPush): OrderBook {
        bids.clear()
        asks.clear()
        instrumentId = push.instId
        mergeLevels(bids, push.payload.bids)
        mergeLevels(asks, push.payload.asks)
        hasSnapshot = true
        return buildOrderBook(push.payload)
    }

    private fun applyUpdate(push: BitgetSocketMessage.OrderBookPush): OrderBook? {
        if (!hasSnapshot || push.instId != instrumentId) return null
        mergeLevels(bids, push.payload.bids)
        mergeLevels(asks, push.payload.asks)
        return buildOrderBook(push.payload)
    }

    // Zero quantity removes the level; otherwise insert or replace it, per Bitget's depth merge rule
    private fun mergeLevels(target: TreeMap<BigDecimal, BigDecimal>, rawLevels: List<List<String>>) {
        for (level in rawLevels) {
            val price = level.getOrNull(0)?.toBigDecimalOrNull() ?: continue
            val quantity = level.getOrNull(1)?.toBigDecimalOrNull() ?: continue
            if (quantity.signum() == 0) target.remove(price) else target[price] = quantity
        }
    }

    private fun buildOrderBook(payload: OrderBookPayloadDto): OrderBook = OrderBook(
        instrumentId = instrumentId,
        bids = bids.map { (price, quantity) -> PriceLevel(price, quantity) },
        asks = asks.map { (price, quantity) -> PriceLevel(price, quantity) },
        sequence = payload.seq,
        timestampMs = payload.ts.toLongOrNull() ?: 0L
    )

    override fun reset() {
        bids.clear()
        asks.clear()
        instrumentId = ""
        hasSnapshot = false
    }
}
