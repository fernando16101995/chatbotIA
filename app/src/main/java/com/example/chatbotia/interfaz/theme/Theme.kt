package com.example.chatbotia.interfaz.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val AppColorScheme = darkColorScheme(
    primary = AccentPrimary,
    onPrimary = TextPrimary,
    primaryContainer = AppSurface,
    onPrimaryContainer = TextPrimary,
    secondary = AccentDim,
    onSecondary = TextPrimary,
    secondaryContainer = AppSurfaceVariant,
    onSecondaryContainer = TextPrimary,
    background = AppBackground,
    onBackground = TextPrimary,
    surface = AppSurface,
    onSurface = TextPrimary,
    surfaceVariant = AppSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    error = ErrorColor,
    onError = TextPrimary,
    outline = DividerColor,
    outlineVariant = DividerColor,
    inverseSurface = TextPrimary,
    inverseOnSurface = AppBackground
)

@Composable
fun ChatbotIATheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AppColorScheme,
        typography = AppTypography,
        content = content
    )
}
