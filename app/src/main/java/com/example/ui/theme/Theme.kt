package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF818CF8),
    onPrimary = Color(0xFF1E1B4B),
    primaryContainer = EditorialIndigoDarkContainer,
    onPrimaryContainer = Color(0xFFE0E7FF),
    secondary = Color(0xFF38BDF8),
    onSecondary = Color.Black,
    secondaryContainer = EditorialBorderDark,
    onSecondaryContainer = EditorialInkDarkTheme,
    background = EditorialBgDark,
    onBackground = EditorialInkDarkTheme,
    surface = EditorialCardDark,
    onSurface = EditorialInkDarkTheme,
    surfaceVariant = EditorialBorderDark,
    onSurfaceVariant = EditorialMutedDarkTheme,
    outline = EditorialBorderDark,
    outlineVariant = Color(0xFF334155),
    error = Color(0xFFFCA5A5),
    onError = Color(0xFF7F1D1D),
    errorContainer = Color(0xFF7F1D1D),
    onErrorContainer = Color(0xFFFEE2E2)
)

private val LightColorScheme = lightColorScheme(
    primary = EditorialIndigo,
    onPrimary = Color.White,
    primaryContainer = EditorialIndigoContainer,
    onPrimaryContainer = Color(0xFF312E81),
    secondary = Sky500,
    onSecondary = Color.White,
    secondaryContainer = SkyLightContainer,
    onSecondaryContainer = SkyLightText,
    background = EditorialBgLight,
    onBackground = EditorialInk,
    surface = EditorialCardLight,
    onSurface = EditorialInk,
    surfaceVariant = EditorialBorderLight,
    onSurfaceVariant = EditorialMuted,
    outline = EditorialBorderLight,
    outlineVariant = Color(0xFFCBD5E1),
    error = ErrorRed,
    onError = Color.White,
    errorContainer = ErrorRedContainer,
    onErrorContainer = ErrorRed
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
