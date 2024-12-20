package com.pfe.maborneapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

object ImageProvider {
    @OptIn(ExperimentalResourceApi::class)
    @Composable
    fun loadLogo(): Painter {
        return painterResource("images/logo.png")
    }
}
