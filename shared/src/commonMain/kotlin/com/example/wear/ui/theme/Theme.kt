package com.example.wear.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val LightColors = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = Color.White,
    primaryContainer = SkyBlueTint,
    onPrimaryContainer = DeepBlue,          // заголовки глибоким синім
    secondary = DeepBlue,
    onSecondary = Color.White,
    secondaryContainer = SkyBlue,           // вибраний чіп = світло-блакитний
    onSecondaryContainer = DeepBlue,
    tertiary = Honey,                       // медовий акцент
    onTertiary = InkText,
    tertiaryContainer = HoneyContainer,
    onTertiaryContainer = Color(0xFF5A3C00),
    background = Cream,
    onBackground = InkText,
    surface = CreamCard,
    onSurface = InkText,
    surfaceVariant = SkyBlueTint,
    onSurfaceVariant = MutedText,
    outline = OutlineSoft,
    error = Coral,
    onError = InkText
)

private val DarkColors = darkColorScheme(
    primary = NightPrimary,
    onPrimary = Color(0xFF08243F),
    primaryContainer = Color(0xFF1F4A78),
    onPrimaryContainer = SkyBlue,
    secondary = SkyBlue,
    onSecondary = Color(0xFF08243F),
    secondaryContainer = Color(0xFF244B73),
    onSecondaryContainer = SkyBlue,
    tertiary = Honey,
    onTertiary = InkText,
    background = NightBg,
    onBackground = NightText,
    surface = NightSurface,
    onSurface = NightText,
    surfaceVariant = Color(0xFF223652),
    onSurfaceVariant = NightMuted,
    outline = Color(0xFF3A4C66),
    error = Coral,
    onError = InkText
)

/** Закруглені форми: картки 24dp, кнопки задаються окремо як «пілюлі». */
private val WearShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(32.dp)
)

@Composable
fun WearTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = wearTypography(),
        shapes = WearShapes,
        content = content
    )
}
