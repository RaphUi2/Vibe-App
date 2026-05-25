package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = VibeSoftPurple,
    onPrimary = VibePrimaryVariant,
    primaryContainer = VibePrimary,
    onPrimaryContainer = VibePrimaryContainer,
    secondary = VibeOrange,
    background = VibeDarkBackground,
    surface = VibeDarkSurface,
    onBackground = VibeDarkOnSurface,
    onSurface = VibeDarkOnSurface,
    onSurfaceVariant = VibeOnSurfaceVariant,
    outline = VibeBorder,
    outlineVariant = VibeBorder.copy(alpha = 0.3f),
    surfaceVariant = VibeDarkSurface
)

private val LightColorScheme = lightColorScheme(
    primary = VibePrimary,
    onPrimary = VibeSurface,
    primaryContainer = VibePrimaryContainer,
    onPrimaryContainer = VibeOnPrimaryContainer,
    secondary = VibeOrange,
    background = VibeBackground,
    surface = VibeSurface,
    onBackground = VibeOnBackground,
    onSurface = VibeOnBackground,
    onSurfaceVariant = VibeOnSurfaceVariant,
    outline = VibeBorder,
    outlineVariant = VibeBorder,
    surfaceVariant = VibeNavigationBar
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // We default dynamicColor to false to prioritize our beautiful deliberate brand colors
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
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
