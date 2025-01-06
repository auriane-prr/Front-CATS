package com.pfe.maborneapp.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.URL

actual suspend fun loadImageBitmap(url: String): ImageBitmap {
    println("DEBUG, loadImageBitmap - Début du téléchargement de l'image")

    // Téléchargement et redimensionnement de l'image
    return withContext(Dispatchers.IO) {
        val inputStream: InputStream = URL(url).openStream()
        try {
            // Options pour redimensionner
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true // Ne charge que les dimensions
            }
            BitmapFactory.decodeStream(inputStream, null, options)

            // Dimensions cibles pour le redimensionnement
            val targetWidth = 1024
            val targetHeight = 1024
            options.inSampleSize = calculateInSampleSize(options, targetWidth, targetHeight)
            options.inJustDecodeBounds = false // Charger l'image complète

            val resizedBitmap = BitmapFactory.decodeStream(URL(url).openStream(), null, options)
                ?: throw Exception("Bitmap non valide ou introuvable")
            println("DEBUG, Dimensions redimensionnées : ${resizedBitmap.width}x${resizedBitmap.height}")

            resizedBitmap.asImageBitmap()
        } finally {
            inputStream.close()
        }
    }
}

// Calcul de l'échantillonnage pour redimensionner
private fun calculateInSampleSize(
    options: BitmapFactory.Options,
    reqWidth: Int,
    reqHeight: Int
): Int {
    val (height: Int, width: Int) = options.outHeight to options.outWidth
    var inSampleSize = 1

    if (height > reqHeight || width > reqWidth) {
        val halfHeight = height / 2
        val halfWidth = width / 2

        // Réduction de l'image tant qu'elle dépasse les dimensions cibles
        while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
            inSampleSize *= 2
        }
    }
    println("DEBUG, inSampleSize calculé : $inSampleSize")
    return inSampleSize
}
