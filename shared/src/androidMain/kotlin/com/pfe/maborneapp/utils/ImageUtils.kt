package com.pfe.maborneapp.utils

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.URL

actual suspend fun loadImageBitmap(url: String): ImageBitmap {
    println("DEBUG, loadImageBitmap - Début du téléchargement : ${System.currentTimeMillis()}")

    // Utilisation de withContext(Dispatchers.IO) pour gérer les opérations bloquantes
    val inputStream: InputStream? = withContext(Dispatchers.IO) {
        try {
            URL(url).openStream()
        } catch (e: Exception) {
            println("Erreur lors de l'ouverture du flux : ${e.message}")
            throw e
        }
    }

    return try {
        // Décodage de l'image en bitmap
        val bitmap = BitmapFactory.decodeStream(inputStream)
        if (bitmap == null || bitmap.width == 0 || bitmap.height == 0) {
            throw Exception("Bitmap invalide ou vide après le téléchargement")
        }
        println("DEBUG, Dimensions de l'image : ${bitmap.width}x${bitmap.height}")
        println("DEBUG, loadImageBitmap - Fin du téléchargement : ${System.currentTimeMillis()}")

        // Conversion en ImageBitmap
        bitmap.asImageBitmap()
    } catch (e: Exception) {
        println("Erreur dans loadImageBitmap : ${e.message}")
        throw e
    } finally {
        // Fermeture du InputStream dans le contexte IO
        withContext(Dispatchers.IO) {
            inputStream?.close()
        }
        println("DEBUG, InputStream fermé.")
    }
}
