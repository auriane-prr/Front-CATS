package com.pfe.maborneapp.utils

import androidx.compose.runtime.Composable
import platform.UIKit.UIScreen
import platform.UIKit.UIUserInterfaceStyle

@Composable
actual fun isSystemInDarkTheme(): Boolean {
    val style = UIScreen.mainScreen.traitCollection.userInterfaceStyle
    return style == UIUserInterfaceStyle.UIUserInterfaceStyleDark
}