package com.pfe.maborneapp.view.user.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Color
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
                    //println("DEBUG, Bitmap chargé : $bitmap")
                    imageBitmap = bitmap
                    println("DEBUG, NetworkImage - Chargement terminé")
                } catch (e: Exception) {
                    println("Erreur lors du chargement de l'image : ${e.message}")
                }
            }
        }

        if (imageBitmap != null) {
            Image(
                bitmap = imageBitmap!!,
                contentDescription = contentDescription,
                modifier = Modifier.fillMaxSize()
            )
        } else {
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

