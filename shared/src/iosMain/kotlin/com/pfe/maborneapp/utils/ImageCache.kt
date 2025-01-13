package com.pfe.maborneapp.utils

import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import org.jetbrains.skia.Image
import platform.Foundation.*
import platform.posix.memcpy

class ImageCache {
    /*private val cache = mutableMapOf<String, Pair<ImageBitmap, String>>() // Map (URL -> (Image, LastModified))
    private val cacheDir = NSSearchPathForDirectoriesInDomains(
        NSCachesDirectory,
        NSUserDomainMask,
        true
    ).firstOrNull() as? String ?: throw IllegalStateException("Cache directory not found")

    actual fun initialize(context: Any?) {
        // Aucune initialisation particulière requise pour iOS
    }

    actual fun getCachedImage(url: String): Pair<ImageBitmap, String>? {
        return cache[url]
    }

    actual fun cacheImage(url: String, imageData: ByteArray, lastModified: String) {
        // Sauvegarde de l'image dans le répertoire de cache
        val filePath = "$cacheDir/${url.hashCode()}"
        val nsData = NSData.create(imageData)
        nsData.writeToFile(filePath, true)

        // Conversion en ImageBitmap
        val image = Image.makeFromEncoded(imageData)
        val bitmap = ImageBitmap(image.width, image.height) // Vous devrez convertir cela

        cache[url] = Pair(bitmap, lastModified)
    }

    actual fun clearCache() {
        val fileManager = NSFileManager.defaultManager
        fileManager.removeItemAtPath(cacheDir, null)
    }*/
}
