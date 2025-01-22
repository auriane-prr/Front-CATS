package com.pfe.maborneapp.utils

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.URL

lateinit var appContext: android.content.Context // Fournir le contexte de l'application

/*actual suspend fun loadImageBitmap(url: String, lastModified: String?): ImageBitmap {
    println("DEBUG, loadImageBitmap - Début du chargement de l'image (URL: $url, LastModified: $lastModified)")

    return withContext(Dispatchers.IO) {
        if (url.startsWith("res://")) {
            // Chargement des ressources locales (sans cache)
            println("DEBUG, loadImageBitmap - Chargement d'une image locale (URL: $url)")
            val resourceName = url.removePrefix("res://images/")
            val assetPath = "images/$resourceName.png"
            val inputStream = appContext.assets.open(assetPath)
            inputStream.use { stream ->
                val imageData = stream.readBytes()
                return@withContext BitmapFactory.decodeByteArray(imageData, 0, imageData.size).asImageBitmap()
            }
        }

        // Chargement des images du backend (avec cache)
        ImageCache.getCachedImage(url)?.let { (cachedImage, cachedLastModified) ->
            if (lastModified != null && cachedLastModified == lastModified) {
                println("DEBUG, loadImageBitmap - Image trouvée dans le cache et valide (URL: $url)")
                return@withContext cachedImage
            } else {
                println("DEBUG, loadImageBitmap - Image obsolète dans le cache ou non trouvée (URL: $url)")
            }
        }

        val inputStream: InputStream = URL(url).openStream()
        val imageData = inputStream.use { it.readBytes() }

        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        BitmapFactory.decodeByteArray(imageData, 0, imageData.size, options)

        options.inSampleSize = calculateInSampleSize(options, 1024, 1024)
        options.inJustDecodeBounds = false

        val imageBitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.size, options).asImageBitmap()

        // Enregistrer l'image dans le cache uniquement si elle provient du backend
        if (lastModified != null) {
            println("DEBUG, loadImageBitmap - Ajout de l'image au cache (URL: $url, LastModified: $lastModified)")
            ImageCache.cacheImage(url, imageData, lastModified)
        }

        imageBitmap
    }
}
*/

actual suspend fun loadImageBitmap(url: String): ImageBitmap {
    println("DEBUG, loadImageBitmap - Chargement de l'image : $url")

    return withContext(Dispatchers.IO) {
        if (url.startsWith("res://")) {
            // Chargement depuis les ressources partagées (assets)
            val resourceName = url.removePrefix("res://images/")
            val assetPath = "images/$resourceName.png" // Chemin dans le dossier assets
            val inputStream = appContext.assets.open(assetPath)
            try {
                val bitmap = BitmapFactory.decodeStream(inputStream)
                    ?: throw Exception("Ressource introuvable : $assetPath")
                bitmap.asImageBitmap()
            } finally {
                inputStream.close()
            }
        } else {
            // Logique existante pour charger depuis le backend
            val inputStream: InputStream = URL(url).openStream()
            try {
                val options = BitmapFactory.Options().apply {
                    inJustDecodeBounds = true
                }
                BitmapFactory.decodeStream(inputStream, null, options)

                val targetWidth = 1024
                val targetHeight = 1024
                options.inSampleSize = calculateInSampleSize(options, targetWidth, targetHeight)
                options.inJustDecodeBounds = false

                val resizedBitmap = BitmapFactory.decodeStream(URL(url).openStream(), null, options)
                    ?: throw Exception("Bitmap non valide ou introuvable")
                resizedBitmap.asImageBitmap()
            } finally {
                inputStream.close()
            }
        }
    }
}

// Calcul de l'échantillonnage pour redimensionner
private fun calculateInSampleSize(
    options: BitmapFactory.Options,
    reqWidth: Int,
    reqHeight: Int
): Int {
    val (height: Int, width) = options.outHeight to options.outWidth
    var inSampleSize = 1

    if (height > reqHeight || width > reqWidth) {
        val halfHeight = height / 2
        val halfWidth = width / 2

        // Réduction de l'image tant qu'elle dépasse les dimensions cibles
        while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
            inSampleSize *= 2
        }
    }
    return inSampleSize
}
