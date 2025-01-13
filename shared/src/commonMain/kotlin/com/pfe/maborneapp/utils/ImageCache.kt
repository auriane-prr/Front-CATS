package com.pfe.maborneapp.utils

import androidx.compose.ui.graphics.ImageBitmap

expect object ImageCache {
    fun initialize(context: Any?)
    fun getCachedImage(url: String): Pair<ImageBitmap, String>?
    fun cacheImage(url: String, imageData: ByteArray, lastModified: String)
    fun clearCache()
}
