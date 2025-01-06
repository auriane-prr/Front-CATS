package com.pfe.maborneapp.utils

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.URL

actual suspend fun loadImageBitmap(url: String): ImageBitmap {
    println("DEBUG, loadImageBitmap - Début du téléchargement de l'image")

    // Téléchargement de l'image en utilisant un flux
    val inputStream: InputStream = withContext(Dispatchers.IO) {
        try {
            URL(url).openStream()
        } catch (e: Exception) {
            println("Erreur lors de l'ouverture du flux : ${e.message}")
            throw e
        }
    }

    return try {
        // Décodage de l'image sans redimensionnement
        val bitmap = BitmapFactory.decodeStream(inputStream)
        if (bitmap == null || bitmap.width == 0 || bitmap.height == 0) {
            throw Exception("Bitmap invalide ou vide après le téléchargement")
        }
        println("DEBUG, Dimensions de l'image téléchargée : ${bitmap.width}x${bitmap.height}")

        // Conversion en ImageBitmap pour Jetpack Compose
        bitmap.asImageBitmap()
    } catch (e: Exception) {
        println("Erreur dans loadImageBitmap : ${e.message}")
        throw e
    } finally {
        // Fermeture du InputStream
        withContext(Dispatchers.IO) {
            inputStream.close()
        }
        println("DEBUG, InputStream fermé.")
    }
}
