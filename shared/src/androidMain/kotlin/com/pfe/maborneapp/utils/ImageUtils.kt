package com.pfe.maborneapp.utils

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.URL

actual suspend fun loadImageBitmap(url: String): ImageBitmap {
    println("DEBUG, loadImageBitmap - Début du téléchargement global : ${System.currentTimeMillis()}")

    // Variable pour mesurer le temps de téléchargement
    val startTime = System.currentTimeMillis()

    // Utilisation de withContext(Dispatchers.IO) pour gérer les opérations bloquantes
    val inputStream: InputStream? = withContext(Dispatchers.IO) {
        try {
            URL(url).openStream()
        } catch (e: Exception) {
            println("Erreur lors de l'ouverture du flux : ${e.message}")
            throw e
        }
    }

    // Log de la durée du téléchargement
    println("DEBUG, Temps de téléchargement : ${System.currentTimeMillis() - startTime} ms")

    return try {
        val options = BitmapFactory.Options().apply {
            inSampleSize = 2 // Divise la résolution par 2
        }
        // Décodage de l'image en bitmap
        val bitmap = BitmapFactory.decodeStream(inputStream, null, options)
        if (bitmap == null || bitmap.width == 0 || bitmap.height == 0) {
            throw Exception("Bitmap invalide ou vide après le téléchargement")
        }
        println("DEBUG, Dimensions de l'image : ${bitmap.width}x${bitmap.height}")
        println("DEBUG, loadImageBitmap - Fin du téléchargement global : ${System.currentTimeMillis()}")

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
