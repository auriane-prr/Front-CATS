package com.pfe.maborneapp.view.components.image

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.pfe.maborneapp.utils.*

@Composable
fun ZoomableImageView(
    imageUrl: String?,
    lastModified: String?,
    contentDescription: String,
    onClose: () -> Unit
) {
    var scale by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var imageAspectRatio by remember { mutableStateOf(1f) }
    val darkModeColorGreen = if (isSystemInDarkTheme()) DarkModeGreen else Color(0xFF045C3C)

    LaunchedEffect(imageUrl, lastModified) {
        if (imageUrl != null) {
            try {
                //imageBitmap = loadImageBitmap(imageUrl, lastModified)
                imageBitmap = loadImageBitmap(imageUrl)
                imageBitmap?.let { bitmap ->
                    imageAspectRatio = bitmap.width.toFloat() / bitmap.height.toFloat()
                }
            } catch (e: Exception) {
                println("Erreur lors du chargement de l'image : ${e.message}")
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column {
            // Carte avec zoom
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color.White)
            ) {
                imageBitmap?.let {
                    Image(
                        bitmap = it,
                        contentDescription = contentDescription,
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer(
                                scaleX = scale,
                                scaleY = scale,
                                translationX = offsetX,
                                translationY = offsetY
                            )
                            .pointerInput(Unit) {
                                detectTransformGestures { _, pan, zoomChange, _ ->
                                    scale *= zoomChange
                                    offsetX += pan.x * 3.0f
                                    offsetY += pan.y * 3.0f
                                }
                            },
                        contentScale = ContentScale.Fit
                    )
                }
            }

            // Boutons de zoom
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .shadow(4.dp, RoundedCornerShape(12.dp))
                    .background(Color.LightGray, RoundedCornerShape(12.dp))
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { scale *= 2.1f },
                    colors = ButtonDefaults.buttonColors(containerColor = darkModeColorGreen)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Zoom In", tint = Color.White)
                }
                Button(
                    onClick = { scale /= 2.1f },
                    colors = ButtonDefaults.buttonColors(containerColor = darkModeColorGreen)
                ) {
                    Icon(Icons.Default.Remove, contentDescription = "Zoom Out", tint = Color.White)
                }
                Button(
                    onClick = onClose,
                    colors = ButtonDefaults.buttonColors(containerColor = darkModeColorGreen)
                ) {
                    Text(text = "Fermer", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.weight(0.1f))
        }
    }
}
