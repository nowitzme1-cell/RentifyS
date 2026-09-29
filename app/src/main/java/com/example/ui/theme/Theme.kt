package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color.White,
    onPrimary = RentifyBlack,
    secondary = RentifyPurple,
    background = RentifyBlack,
    surface = Color(0xFF18181B),
    onBackground = Color.White,
    onSurface = Color.White,
    outline = Color(0xFF27272A),
    outlineVariant = Color(0xFF3F3F46)
)

private val LightColorScheme = lightColorScheme(
    primary = RentifyBlack,
    onPrimary = Color.White,
    secondary = RentifyPurple,
    onSecondary = Color.White,
    tertiary = VerifiedBlue,
    background = RentifyPageBg,
    surface = RentifyCardBg,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    surfaceVariant = Color(0xFFF4F4F5),
    onSurfaceVariant = TextSecondary,
    outline = RentifyBorderDark,
    outlineVariant = RentifyBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Set false to preserve our custom luxury Airbnb/Apple palette
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
