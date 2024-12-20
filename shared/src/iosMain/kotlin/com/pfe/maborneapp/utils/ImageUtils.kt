package com.pfe.maborneapp.utils

import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreGraphics.CGImageGetHeight
import platform.CoreGraphics.CGImageGetWidth
import platform.Foundation.NSData
import platform.Foundation.NSURL
import platform.Foundation.dataWithContentsOfURL
import platform.UIKit.UIImage

actual suspend fun loadImageBitmap(url: String): ImageBitmap {
    println("DEBUG, loadImageBitmap - URL reçue : $url")
    return try {
        val nsUrl = NSURL.URLWithString(url) ?: throw IllegalArgumentException("URL invalide")
        val data = NSData.dataWithContentsOfURL(nsUrl) ?: throw Exception("Impossible de télécharger l'image")
        val uiImage = UIImage.imageWithData(data) ?: throw Exception("Impossible de convertir les données en UIImage")
        println("DEBUG, loadImageBitmap - Image chargée avec succès")
        uiImage.toImageBitmap()
    } catch (e: Exception) {
        println("Erreur dans loadImageBitmap : ${e.message}")
        throw e
    }
}

// Extension pour convertir UIImage en ImageBitmap
@OptIn(ExperimentalForeignApi::class)
fun UIImage.toImageBitmap(): ImageBitmap {
    val cgImage = this.CGImage ?: throw IllegalArgumentException("UIImage n'a pas de CGImage")

    // Utilisation des fonctions CoreGraphics pour récupérer width et height
    val width = CGImageGetWidth(cgImage).toInt()
    val height = CGImageGetHeight(cgImage).toInt()

    return ImageBitmap(width, height) // Simplification, dépend des besoins spécifiques
}

