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
    primary = WaTeal,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF005C4B),
    onPrimaryContainer = Color(0xFFD9FDD3),
    secondary = Color(0xFF34B7F1),
    onSecondary = Color.Black,
    secondaryContainer = WaDarkSurfaceVariant,
    onSecondaryContainer = WaDarkText,
    background = WaDarkBg,
    onBackground = WaDarkText,
    surface = WaDarkSurface,
    onSurface = WaDarkText,
    surfaceVariant = WaDarkSurfaceVariant,
    onSurfaceVariant = WaDarkSubtext,
    error = Color(0xFFF8D7DA),
    onError = Color(0xFF842029),
    errorContainer = Color(0xFF842029),
    onErrorContainer = Color(0xFFF8D7DA)
)

private val LightColorScheme = lightColorScheme(
    primary = FbBlue,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE7F3FF),
    onPrimaryContainer = Color(0xFF0D47A1),
    secondary = Sky500,
    onSecondary = Color.White,
    secondaryContainer = FbLightSurfaceVariant,
    onSecondaryContainer = FbLightText,
    background = FbLightBg,
    onBackground = FbLightText,
    surface = FbLightSurface,
    onSurface = FbLightText,
    surfaceVariant = FbLightSurfaceVariant,
    onSurfaceVariant = FbLightSubtext,
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
