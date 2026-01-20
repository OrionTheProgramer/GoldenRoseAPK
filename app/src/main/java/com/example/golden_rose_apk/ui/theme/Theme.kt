package com.example.golden_rose_apk.ui.theme


import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = ValorantRed,
    secondary = GoldenAccent,
    tertiary = GoldenAccent,
    background = DarkBackground,
    surface = DarkSurface,
    surfaceVariant = DarkSurface,
    onPrimary = TextPrimary,
    onSecondary = Color.Black,
    onTertiary = Color.Black,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    onSurfaceVariant = TextSecondary,
    outline = BorderColor
)

private val LightColorScheme = lightColorScheme(
    primary = ValorantRed,
    secondary = Color(0xFF6D5DD3),
    tertiary = GoldenAccent,
    background = Color(0xFFF5F6FA),
    surface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFFE7ECF3),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color(0xFF2D2A32),
    onBackground = Color(0xFF0F1923),
    onSurface = Color(0xFF1C1B1F),
    onSurfaceVariant = Color(0xFF475569),
    outline = Color(0xFFCDD5DF)
)

@Composable
fun GoldenRoseTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
