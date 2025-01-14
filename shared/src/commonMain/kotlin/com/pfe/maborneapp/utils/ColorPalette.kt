package com.pfe.maborneapp.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme

val LightThemeColors = lightColorScheme(
    primary = Color(0xFF6200EE),
    secondary = Color.White,
    background = Color(0xFFFFFFFF),
    surface = Color(0xFFFFFFFF),
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
)

val DarkThemeColors = darkColorScheme(
    primary = Color(0xFF424242),
    secondary = Color(0xFF03DAC6),
    background = Color(0xFF121212),
    surface = Color(0xFF121212),
    onPrimary = Color.Black,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
)

// Définir une couleur de container personnalisée pour le mode sombre
val DarkContainerColor = Color(0xFF424242)

val DarkModeGreen = Color(0xFF84AD9D)
