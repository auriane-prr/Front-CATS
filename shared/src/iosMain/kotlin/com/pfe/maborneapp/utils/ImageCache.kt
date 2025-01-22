package com.pfe.maborneapp.utils

import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.cinterop.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import platform.Foundation.*
import platform.UIKit.UIImage

actual object ImageCache {
    private val cache = mutableMapOf<String, Pair<ImageBitmap, String>>() // Map (URL -> (ImageBitmap, LastModified))

    private val cacheDirName = "image_cache"
    private val metaFileName = "image_metadata.json"

    private val fileManager: NSFileManager = NSFileManager.defaultManager

    private fun getCacheDir(): String {
        val cacheDir = NSSearchPathForDirectoriesInDomains(
            NSCachesDirectory,
            NSUserDomainMask,
            true
        ).firstOrNull() as? String ?: throw IllegalStateException("Failed to get cache directory")
        return "$cacheDir/$cacheDirName"
    }

    @OptIn(ExperimentalForeignApi::class)
    actual fun initialize(context: Any?) {
        // Créer le dossier de cache si nécessaire
        val cacheDir = getCacheDir()
        if (!fileManager.fileExistsAtPath(cacheDir)) {
            fileManager.createDirectoryAtPath(cacheDir, withIntermediateDirectories = true, attributes = null, error = null)
        }
        loadCache()
    }

    actual fun getCachedImage(url: String): Pair<ImageBitmap, String>? {
        return cache[url]
    }

    @OptIn(ExperimentalForeignApi::class)
    actual fun cacheImage(url: String, imageData: ByteArray, lastModified: String) {
        val cacheDir = getCacheDir()
        val imageFilePath = "$cacheDir/${url.hashCode()}"

        // Écriture de l'image sur le disque
        NSData.dataWithBytes(imageData.refTo(0).getPointer(MemScope()), imageData.size.toULong()).writeToFile(imageFilePath, atomically = true)

        // Conversion des données en UIImage puis en ImageBitmap
        val uiImage = UIImage.imageWithContentsOfFile(imageFilePath)
            ?: throw IllegalArgumentException("Failed to decode image from file")
        val imageBitmap = uiImage.toImageBitmap()

        // Mise à jour du cache en mémoire
        cache[url] = Pair(imageBitmap, lastModified)
        saveMetadata()
    }

    @OptIn(ExperimentalForeignApi::class)
    actual fun clearCache() {
        cache.clear()

        val cacheDir = getCacheDir()
        fileManager.removeItemAtPath(cacheDir, null)

        saveMetadata()
    }

    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    private fun saveMetadata() {
        val metadata = cache.mapValues { it.value.second }
        val metadataJson = Json.encodeToString(metadata)
        val metaFilePath = "${getCacheDir()}/$metaFileName"

        val byteArray = metadataJson.encodeToByteArray()
        byteArray.usePinned {
            NSData.dataWithBytes(it.addressOf(0), byteArray.size.toULong())
                .writeToFile(metaFilePath, atomically = true)
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun loadCache() {
        val metaFilePath = "${getCacheDir()}/$metaFileName"
        if (!fileManager.fileExistsAtPath(metaFilePath)) return

        try {
            val metadataJson = NSString.stringWithContentsOfFile(
                metaFilePath,
                encoding = NSUTF8StringEncoding,
                error = null
            )

            metadataJson?.let { Json.decodeFromString<Map<String, String>?>(it) }
                ?.forEach { (url, lastModified) ->
                    val imageFilePath = "${getCacheDir()}/${url.hashCode()}"
                    if (fileManager.fileExistsAtPath(imageFilePath)) {
                        val uiImage = UIImage.imageWithContentsOfFile(imageFilePath)
                        if (uiImage != null) {
                            val imageBitmap = uiImage.toImageBitmap()
                            cache[url] = Pair(imageBitmap, lastModified)
                        }
                    }
                }
        } catch (e: Exception) {
            println("Erreur lors du chargement du cache : ${e.message}")
        }
    }
}
