package com.pfe.maborneapp.utils

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileOutputStream

actual object ImageCache {
    private val cache = mutableMapOf<String, Pair<ImageBitmap, String>>() // Map (URL -> (Image, LastModified))
    private lateinit var appContext: Context

    private val cacheDirName = "image_cache"
    private val metaFileName = "image_metadata.json"

    fun getCacheDir(): File = File(appContext.cacheDir, cacheDirName)

    actual fun initialize(context: Any?) {
        if (context is Context) {
            appContext = context
            loadCache() // Charger le cache depuis le disque
        } else {
            throw IllegalArgumentException("Expected Android Context")
        }
    }

    actual fun getCachedImage(url: String): Pair<ImageBitmap, String>? {
        return cache[url]
    }

    actual fun cacheImage(url: String, imageData: ByteArray, lastModified: String) {
        // Sauvegarde de l'image sur le disque
        val cacheDir = getCacheDir()
        if (!cacheDir.exists()) cacheDir.mkdirs()

        val imageFile = File(cacheDir, url.hashCode().toString())
        FileOutputStream(imageFile).use {
            it.write(imageData)
        }

        // Conversion en ImageBitmap
        val bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.size).asImageBitmap()

        // Mise à jour en mémoire et sauvegarde des métadonnées
        cache[url] = Pair(bitmap, lastModified)
        saveMetadata()
    }

    actual fun clearCache() {
        cache.clear()
        getCacheDir().deleteRecursively()
        saveMetadata()
    }

    private fun saveMetadata() {
        // Convertir les métadonnées en JSON
        val metadata = cache.mapValues { it.value.second }
        val metaFile = File(appContext.cacheDir, metaFileName)
        metaFile.writeText(Json.encodeToString(metadata))
    }

    private fun loadCache() {
        val metaFile = File(appContext.cacheDir, metaFileName)
        if (!metaFile.exists()) return

        try {
            // Charger les métadonnées depuis le fichier JSON
            val metadata: Map<String, String> = Json.decodeFromString(metaFile.readText())
            val cacheDir = getCacheDir()

            metadata.forEach { (url, lastModified) ->
                val imageFile = File(cacheDir, url.hashCode().toString())
                if (imageFile.exists()) {
                    val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath).asImageBitmap()
                    cache[url] = Pair(bitmap, lastModified)
                }
            }
        } catch (e: Exception) {
            println("Erreur lors du chargement du cache : ${e.message}")
        }
    }
}
