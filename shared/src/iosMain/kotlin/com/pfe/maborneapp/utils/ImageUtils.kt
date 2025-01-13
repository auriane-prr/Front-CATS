package com.pfe.maborneapp.utils

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toComposeImageBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import org.jetbrains.skia.Surface

actual suspend fun loadImageBitmap(url: String, lastModified: String?): ImageBitmap {
    println("DEBUG, loadImageBitmap (iOS) - Fonction non implémentée pour iOS. URL: $url, LastModified: $lastModified")

    // Retourne une image vide temporaire
    return withContext(Dispatchers.IO) {
        val width = 100
        val height = 100
        val surface = Surface.makeRasterN32Premul(width, height)
        surface.canvas.clear(org.jetbrains.skia.Color.WHITE)
        surface.canvas.drawCircle(50f, 50f, 40f, org.jetbrains.skia.Paint().apply { color = org.jetbrains.skia.Color.BLACK })
        surface.makeImageSnapshot().toComposeImageBitmap()
    }
}
