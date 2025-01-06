package com.pfe.maborneapp.view.user.components

import androidx.compose.foundation.Image
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
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import com.pfe.maborneapp.utils.loadImageBitmap

@Composable
fun NetworkImage(imageUrl: String?, contentDescription: String, modifier: Modifier = Modifier) {
    println("DEBUG, NetworkImage - URL reçue : $imageUrl")
    if (imageUrl != null) {
        var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(imageUrl) {
            coroutineScope.launch {
                try {
                    println("DEBUG, NetworkImage - Chargement de l'image depuis : $imageUrl")
                    val bitmap = loadImageBitmap(imageUrl)
                    imageBitmap = bitmap
                    println("DEBUG, NetworkImage - Chargement terminé")
                } catch (e: Exception) {
                    println("Erreur lors du chargement de l'image : ${e.message}")
                }
            }
        }

        imageBitmap?.let {
            val aspectRatio = it.width.toFloat() / it.height.toFloat()
            println("DEBUG, Rendu de l'image avec aspectRatio : $aspectRatio")

            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .aspectRatio(aspectRatio),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    bitmap = it,
                    contentDescription = contentDescription,
                    modifier = Modifier
                        .fillMaxWidth() // Occupe toute la largeur de l'écran
                        .aspectRatio(1f), // Ajustez l'aspect ratio si nécessaire
                    contentScale = ContentScale.Fit // Permet à l'image de s'adapter sans être coupée
                )

            }
        } ?: run {
            Box(
                contentAlignment = Alignment.Center,
                modifier = modifier
            ) {
                Text(
                    text = "Chargement de l'image...",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        }
    } else {
        Text(
            text = "Aucune image disponible",
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = modifier
        )
    }
}
