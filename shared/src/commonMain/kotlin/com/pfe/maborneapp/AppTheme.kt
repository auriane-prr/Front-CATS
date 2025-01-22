package com.pfe.maborneapp

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.pfe.maborneapp.utils.DarkThemeColors
import com.pfe.maborneapp.utils.LightThemeColors
import com.pfe.maborneapp.utils.isSystemInDarkTheme

@Composable
fun AppTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) DarkThemeColors else LightThemeColors

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}