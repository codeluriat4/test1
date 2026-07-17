package com.cian.orderbook.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val CianColorScheme = darkColorScheme(
    background = BackgroundDark,
    surface = SurfaceDark,
    surfaceVariant = SurfaceRaised,
    onBackground = OnBackgroundDark,
    onSurface = OnSurfaceDark,
    primary = HeatmapHigh,
    secondary = HeatmapLow,
    error = AskRed
)

@Composable
fun CianTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = CianColorScheme,
        typography = CianTypography,
        shapes = CianShapes,
        content = content
    )
}
