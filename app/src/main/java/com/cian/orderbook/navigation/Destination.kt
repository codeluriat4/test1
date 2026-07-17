package com.cian.orderbook.navigation

// Single source of truth for route names, avoids magic strings scattered across the codebase
sealed interface Destination {
    val route: String

    data object Home : Destination {
        override val route: String = "home"
    }
}
