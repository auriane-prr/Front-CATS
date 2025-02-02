package com.pfe.maborneapp.view.components.image

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import com.pfe.maborneapp.utils.loadImageBitmap
import kotlinx.coroutines.launch

@Composable
fun NetworkImage(
    imageUrl: String?,
    //lastModified: String? = null,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var imageAspectRatio by remember { mutableStateOf(1f) } // Ratio par défaut 1:1

    val coroutineScope = rememberCoroutineScope()

    /*LaunchedEffect(imageUrl) {
        if (imageUrl != null && lastModified != null) {
            coroutineScope.launch {
                try {
                    println("DEBUG, NetworkImage - Chargement de l'image depuis : $imageUrl")
                    val bitmap = loadImageBitmap(imageUrl, lastModified)
                    imageBitmap = bitmap
                    // Calcul dynamique du ratio largeur/hauteur
                    imageAspectRatio = bitmap.width.toFloat() / bitmap.height.toFloat()
                    println("DEBUG, Ratio calculé : $imageAspectRatio")
                } catch (e: Exception) {
                    println("Erreur lors du chargement de l'image : ${e.message}")
                }
            }
        }
    }*/

    LaunchedEffect(imageUrl) {
        if (imageUrl != null) {
            coroutineScope.launch {
                try {
                    println("DEBUG, NetworkImage - Chargement de l'image depuis : $imageUrl")
                    val bitmap = loadImageBitmap(imageUrl)
                    imageBitmap = bitmap
                    imageBitmap?.let {
                        imageAspectRatio = it.width.toFloat() / it.height.toFloat()
                        println("DEBUG, Ratio calculé : $imageAspectRatio")
                    } ?: println("DEBUG, NetworkImage - Image introuvable : $imageUrl")
                } catch (e: Exception) {
                    println("Erreur lors du chargement de l'image : ${e.message}")
                }
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(imageAspectRatio)
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        imageBitmap?.let {
            Image(
                bitmap = it,
                contentDescription = contentDescription,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        } ?: run {
            Text(
                text = "Chargement...",
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}
