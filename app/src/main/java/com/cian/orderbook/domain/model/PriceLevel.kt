package com.cian.orderbook.domain.model

import java.math.BigDecimal

// A single depth entry; price is the level key, quantity is the resting size at that level
data class PriceLevel(
    val price: BigDecimal,
    val quantity: BigDecimal
)
