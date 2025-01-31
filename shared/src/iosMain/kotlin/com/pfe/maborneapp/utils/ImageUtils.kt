package com.pfe.maborneapp.utils

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import kotlinx.cinterop.*
import platform.CoreGraphics.*
import platform.Foundation.*
import platform.UIKit.*
import platform.posix.memcpy

/*actual suspend fun loadImageBitmap(url: String, lastModified: String?): ImageBitmap {
    println("DEBUG, loadImageBitmap - Début du chargement de l'image (URL: $url)")

    return if (url.startsWith("res://")) {
        // Chargement des ressources locales
        println("DEBUG, loadImageBitmap - Chargement d'une image locale (URL: $url)")
        val resourceName = url.removePrefix("res://images/")
        val uiImage = UIImage(resourceName)
            ?: throw IllegalArgumentException("Image resource not found: $resourceName")
        uiImage.toImageBitmap()
    } else {
        // Vérification dans le cache
        ImageCache.getCachedImage(url)?.let { (cachedImage, cachedLastModified) ->
            if (lastModified != null && cachedLastModified == lastModified) {
                println("DEBUG, loadImageBitmap - Image trouvée dans le cache et valide (URL: $url)")
                return cachedImage
            } else {
                println("DEBUG, loadImageBitmap - Image obsolète dans le cache ou non trouvée (URL: $url)")
            }
        }

        // Chargement des images du backend
        println("DEBUG, loadImageBitmap - Chargement d'une image distante (URL: $url)")
        val nsUrl = NSURL.URLWithString(url) ?: throw IllegalArgumentException("Invalid URL: $url")
        val imageData = NSData.dataWithContentsOfURL(nsUrl)
            ?: throw IllegalArgumentException("Failed to load image from URL: $url")
        val uiImage = UIImage.imageWithData(imageData)
            ?: throw IllegalArgumentException("Failed to decode image from URL: $url")
        val imageBitmap = uiImage.toImageBitmap()

        // Ajout de l'image dans le cache
        if (lastModified != null) {
            println("DEBUG, loadImageBitmap - Ajout de l'image au cache (URL: $url, LastModified: $lastModified)")
            ImageCache.cacheImage(url, imageData.toByteArray(), lastModified)
        }

        imageBitmap
    }
}

@OptIn(ExperimentalForeignApi::class)
fun UIImage.toImageBitmap(): ImageBitmap {
    val cgImage = this.CGImage ?: throw IllegalArgumentException("UIImage does not contain a CGImage")

    val width = CGImageGetWidth(cgImage).toInt()
    val height = CGImageGetHeight(cgImage).toInt()

    val colorSpace = CGColorSpaceCreateDeviceRGB()
    val bytesPerPixel = 4
    val bytesPerRow = bytesPerPixel * width
    val bitsPerComponent = 8

    // Allocation de mémoire pour les données d'image
    val data = ByteArray(bytesPerRow * height)

    val context = CGBitmapContextCreate(
        data.refTo(0), // Pointeur vers les données
        width.convert(),
        height.convert(),
        bitsPerComponent.convert(),
        bytesPerRow.convert(),
        colorSpace,
        CGImageAlphaInfo.kCGImageAlphaPremultipliedLast.value or kCGBitmapByteOrder32Big
    ) ?: throw IllegalArgumentException("Failed to create CGBitmapContext")

    // Dessin de l'image dans le contexte
    CGContextDrawImage(context, CGRectMake(0.0, 0.0, width.toDouble(), height.toDouble()), cgImage)

    // Conversion des données en ImageBitmap
    return org.jetbrains.skia.Image.makeRaster(
        org.jetbrains.skia.ImageInfo.makeN32Premul(width, height),
        data,
        bytesPerRow
    ).toComposeImageBitmap()
}

@OptIn(ExperimentalForeignApi::class)
private fun NSData.toByteArray(): ByteArray {
    val size = this.length.toInt() // Taille des données
    val byteArray = ByteArray(size) // Création du tableau de bytes
    byteArray.usePinned { pinned ->
        memcpy(pinned.addressOf(0), this.bytes, size.convert()) // Copie des données dans le tableau
    }
    return byteArray
}
*/
actual suspend fun loadImageBitmap(url: String): ImageBitmap? {
    return try {
        if (url.startsWith("res://")) {
            // Chargement des ressources locales
            println("DEBUG, loadImageBitmap - Chargement d'une image locale (URL: $url)")
            val resourceName = url.removePrefix("res://images/")
            val uiImage = UIImage(resourceName)
                ?: throw IllegalArgumentException("Image resource not found: $resourceName")
            uiImage.toImageBitmap()
        } else {
            // Chargement des images du backend
            println("DEBUG, loadImageBitmap - Chargement d'une image distante (URL: $url)")
            val nsUrl = NSURL.URLWithString(url) ?: throw IllegalArgumentException("Invalid URL: $url")
            val imageData = NSData.dataWithContentsOfURL(nsUrl)
                ?: throw IllegalArgumentException("Failed to load image from URL: $url")
            val uiImage = UIImage.imageWithData(imageData)
                ?: throw IllegalArgumentException("Failed to decode image from URL: $url")
            uiImage.toImageBitmap()
        }
    } catch (e: Exception) {
        println("Erreur lors du chargement de l'image : ${e.message}")
        null // Retourne null en cas d'erreur
    }
}

@OptIn(ExperimentalForeignApi::class)
fun UIImage.toImageBitmap(): ImageBitmap {
    val cgImage = this.CGImage ?: throw IllegalArgumentException("UIImage does not contain a CGImage")

    val width = CGImageGetWidth(cgImage).toInt()
    val height = CGImageGetHeight(cgImage).toInt()

    val colorSpace = CGColorSpaceCreateDeviceRGB()
    val bytesPerPixel = 4
    val bytesPerRow = bytesPerPixel * width
    val bitsPerComponent = 8

    // Allocation de mémoire pour les données d'image
    val data = ByteArray(bytesPerRow * height)

    val context = CGBitmapContextCreate(
        data.refTo(0), // Pointeur vers les données
        width.convert(),
        height.convert(),
        bitsPerComponent.convert(),
        bytesPerRow.convert(),
        colorSpace,
        CGImageAlphaInfo.kCGImageAlphaPremultipliedLast.value or kCGBitmapByteOrder32Big
    ) ?: throw IllegalArgumentException("Failed to create CGBitmapContext")

    // Dessin de l'image dans le contexte
    CGContextDrawImage(context, CGRectMake(0.0, 0.0, width.toDouble(), height.toDouble()), cgImage)

    // Conversion des données en ImageBitmap
    return org.jetbrains.skia.Image.makeRaster(
        org.jetbrains.skia.ImageInfo.makeN32Premul(width, height),
        data,
        bytesPerRow
    ).toComposeImageBitmap()
}
