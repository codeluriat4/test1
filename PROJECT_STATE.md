# PROJECT_STATE.md

## Phase
Skeleton — compiles, launches to an empty dark-themed screen.

## Stack
- Kotlin 2.3.x, Jetpack Compose, Material3
- Gradle 9.4.1 / AGP 9.2.x, compileSdk 37, targetSdk 30, minSdk 28, JVM target 21
- Hilt (DI), Navigation Compose, OkHttp (WebSocket client, unused yet), Kotlinx Coroutines

## Package structure
```
com.cian.orderbook
├── CianApplication.kt        — @HiltAndroidApp entry point
├── MainActivity.kt           — hosts CianNavHost inside CianTheme
├── core/dispatcher/          — DispatcherProvider interface + DefaultDispatcherProvider
├── di/                       — AppModule (dispatcher binding), NetworkModule (OkHttpClient)
├── navigation/                — Destination sealed interface, CianNavHost
└── ui/
    ├── theme/                — Color, Shape, Type, Theme (dark neumorphic base tokens)
    └── screens/home/         — HomeScreen (empty canvas), HomeViewModel, HomeUiState
```

## Done
- [x] Gradle configuration (version catalog, root + app build scripts)
- [x] Dark theme scaffold (Material3 darkColorScheme, neumorphic color/shape tokens)
- [x] Navigation graph with single Home route
- [x] Hilt DI scaffold (Application, AppModule, NetworkModule, dispatcher abstraction)
- [x] Empty Home screen — compiles and launches to a blank dark canvas

## Not started
- [ ] WebSocket client for Bitget BTCUSDTP order book stream
- [ ] Candlestick chart rendering (100-candle window, all timeframes)
- [ ] Heatmap overlay (blue → yellow intensity gradient, node length = order duration)
- [ ] Tap-to-inspect popup for heatmap zones
- [ ] Scroll-driven chart collapse + analytics panel (aggregate volume, top 10 zones)
- [ ] Skeleton loading screen → app transition
- [ ] Timeframe toggle controls (1m / 5m / 15m / 30m / 1h)

## Notes
- Gradle Wrapper files are assumed to exist externally and are intentionally not generated here.
- No historical data persistence by design — the app is live-data-only per spec.
- applicationId / package: `com.cian.orderbook`.
