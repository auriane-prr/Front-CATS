package com.pfe.maborneapp.utils

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.refTo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import org.jetbrains.skia.Image
import platform.Foundation.NSData
import platform.Foundation.NSURL
import platform.Foundation.dataWithContentsOfURL
import platform.UIKit.UIImage
import platform.UIKit.UIImagePNGRepresentation
import platform.Foundation.NSLog
import platform.posix.memcpy

actual suspend fun loadImageBitmap(url: String): ImageBitmap {
    NSLog("DEBUG, loadImageBitmap - Chargement de l'image : $url")

    return withContext(Dispatchers.IO) {
        try {
            if (url.startsWith("res://")) {
                // Charger depuis le catalogue d'assets d'Xcode
                val resourceName = url.removePrefix("res://images/")
                val uiImage = UIImage.imageNamed(resourceName)
                    ?: throw Exception("Ressource introuvable dans les assets : $resourceName")
                NSLog("DEBUG, loadImageBitmap - Image trouvée dans les assets : $resourceName")

                val nsData = uiImage.toNSData()
                val skiaImage = Image.makeFromEncoded(nsData.toByteArray())
                skiaImage.toComposeImageBitmap()
            } else {
                // Charger depuis une URL externe
                val nsUrl = NSURL.URLWithString(url) ?: throw IllegalArgumentException("URL invalide : $url")
                NSLog("DEBUG, loadImageBitmap - URL valide : $url")

                val data = NSData.dataWithContentsOfURL(nsUrl)
                    ?: throw Exception("Impossible de télécharger l'image depuis : $url")
                NSLog("DEBUG, loadImageBitmap - Données téléchargées avec succès depuis l'URL")

                val skiaImage = Image.makeFromEncoded(data.toByteArray())
                skiaImage.toComposeImageBitmap()
            }
        } catch (e: Exception) {
            NSLog("ERROR, loadImageBitmap - Une erreur s'est produite : ${e.message}")
            throw e
        }
    }
}

// Extension pour convertir UIImage en NSData
@OptIn(ExperimentalForeignApi::class)
fun UIImage.toNSData(): NSData {
    val data = UIImagePNGRepresentation(this)
        ?: throw Exception("Impossible de convertir UIImage en NSData")
    NSLog("DEBUG, toNSData - Conversion de UIImage en NSData réussie")
    return data
}

// Extension pour convertir NSData en ByteArray
@OptIn(ExperimentalForeignApi::class)
fun NSData.toByteArray(): ByteArray {
    NSLog("DEBUG, toByteArray - Conversion de NSData en ByteArray")
    val length = this.length.toInt()
    val byteArray = ByteArray(length)
    memcpy(byteArray.refTo(0), this.bytes, length.toULong())
    NSLog("DEBUG, toByteArray - Conversion réussie avec une taille de $length octets")
    return byteArray
}
