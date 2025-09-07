package com.pixeldev.composetv.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.darkColorScheme
import androidx.tv.material3.lightColorScheme

private val darkScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark
)

// 🎨 Brand Colors
val PurpleGradient = Color(0xFF9C27FF)
val BlueGradient = Color(0xFF2196F3)
val DarkBackground = Color(0xFF0A0F1F)
val AccentDark = Color(0xFF1B1F3A)
val WhiteText = Color(0xFFFFFFFF)

// 🌙 Dark Color Scheme
private val DarkColorScheme = darkColorScheme(
    primary = BlueGradient,
    onPrimary = WhiteText,
    secondary = PurpleGradient,
    onSecondary = WhiteText,
    background = DarkBackground,
    onBackground = WhiteText,
    surface = AccentDark,
    onSurface = WhiteText,
    error = Color(0xFFFF6B6B),
    onError = WhiteText
)
@Composable
fun ComposeTVYTTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        shapes = MaterialTheme.shapes,
        typography = Typography,
        content = content
    )
}
