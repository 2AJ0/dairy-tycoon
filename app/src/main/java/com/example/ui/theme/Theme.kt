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
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.material3.ColorScheme

private val DarkColorScheme = darkColorScheme(
    primary = DairyEmeraldLight,
    onPrimary = Color(0xFF003822),
    primaryContainer = DairyEmeraldDark,
    onPrimaryContainer = Color(0xFFA7F3D0),
    secondary = DairyGold,
    onSecondary = Color(0xFF451A03),
    secondaryContainer = Color(0xFF78350F),
    onSecondaryContainer = DairyCream,
    tertiary = TechCyan,
    background = DairySlateDark,
    onBackground = Color(0xFFF1F5F9),
    surface = DairySlateCardDark,
    onSurface = Color(0xFFF8FAFC),
    surfaceVariant = Color(0xFF334155),
    onSurfaceVariant = Color(0xFFCBD5E1),
    error = BearishRed
)

private val LightColorScheme = lightColorScheme(
    primary = DairyEmeraldPrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD1FAE5),
    onPrimaryContainer = DairyEmeraldDark,
    secondary = DairyGoldDark,
    onSecondary = Color.White,
    secondaryContainer = DairyCreamSurface,
    onSecondaryContainer = Color(0xFF78350F),
    tertiary = TechCyan,
    background = Color(0xFFF8FAFC),
    onBackground = Color(0xFF0F172A),
    surface = DairySlateCardLight,
    onSurface = Color(0xFF1E293B),
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = Color(0xFF475569),
    error = BearishRed
)


@Composable
fun animatedColorScheme(target: ColorScheme): ColorScheme {
    val spec = tween<Color>(1500) // 1.5 seconds smooth transition
    return ColorScheme(
        primary = animateColorAsState(target.primary, spec).value,
        onPrimary = animateColorAsState(target.onPrimary, spec).value,
        primaryContainer = animateColorAsState(target.primaryContainer, spec).value,
        onPrimaryContainer = animateColorAsState(target.onPrimaryContainer, spec).value,
        inversePrimary = animateColorAsState(target.inversePrimary, spec).value,
        secondary = animateColorAsState(target.secondary, spec).value,
        onSecondary = animateColorAsState(target.onSecondary, spec).value,
        secondaryContainer = animateColorAsState(target.secondaryContainer, spec).value,
        onSecondaryContainer = animateColorAsState(target.onSecondaryContainer, spec).value,
        tertiary = animateColorAsState(target.tertiary, spec).value,
        onTertiary = animateColorAsState(target.onTertiary, spec).value,
        tertiaryContainer = animateColorAsState(target.tertiaryContainer, spec).value,
        onTertiaryContainer = animateColorAsState(target.onTertiaryContainer, spec).value,
        background = animateColorAsState(target.background, spec).value,
        onBackground = animateColorAsState(target.onBackground, spec).value,
        surface = animateColorAsState(target.surface, spec).value,
        onSurface = animateColorAsState(target.onSurface, spec).value,
        surfaceVariant = animateColorAsState(target.surfaceVariant, spec).value,
        onSurfaceVariant = animateColorAsState(target.onSurfaceVariant, spec).value,
        surfaceTint = animateColorAsState(target.surfaceTint, spec).value,
        inverseSurface = animateColorAsState(target.inverseSurface, spec).value,
        inverseOnSurface = animateColorAsState(target.inverseOnSurface, spec).value,
        error = animateColorAsState(target.error, spec).value,
        onError = animateColorAsState(target.onError, spec).value,
        errorContainer = animateColorAsState(target.errorContainer, spec).value,
        onErrorContainer = animateColorAsState(target.onErrorContainer, spec).value,
        outline = animateColorAsState(target.outline, spec).value,
        outlineVariant = animateColorAsState(target.outlineVariant, spec).value,
        scrim = animateColorAsState(target.scrim, spec).value,
        surfaceBright = animateColorAsState(target.surfaceBright, spec).value,
        surfaceDim = animateColorAsState(target.surfaceDim, spec).value,
        surfaceContainer = animateColorAsState(target.surfaceContainer, spec).value,
        surfaceContainerHigh = animateColorAsState(target.surfaceContainerHigh, spec).value,
        surfaceContainerHighest = animateColorAsState(target.surfaceContainerHighest, spec).value,
        surfaceContainerLow = animateColorAsState(target.surfaceContainerLow, spec).value,
        surfaceContainerLowest = animateColorAsState(target.surfaceContainerLowest, spec).value
    )
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep consistent branding colors
    content: @Composable () -> Unit
) {
    val targetColorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val animatedColors = animatedColorScheme(targetColorScheme)

    MaterialTheme(
        colorScheme = animatedColors,
        typography = Typography,
        content = content
    )
}


