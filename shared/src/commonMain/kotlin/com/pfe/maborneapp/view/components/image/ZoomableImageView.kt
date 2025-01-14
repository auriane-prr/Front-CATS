package com.pfe.maborneapp.view.components.image

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.pfe.maborneapp.utils.DarkModeGreen
import com.pfe.maborneapp.utils.loadImageBitmap
@Composable
fun ZoomableImageView(
    imageUrl: String?,
    lastModified: String?,
    contentDescription: String,
    onClose: () -> Unit
) {
    var scale by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) } // Décalage horizontal
    var offsetY by remember { mutableStateOf(0f) } // Décalage vertical
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var imageAspectRatio by remember { mutableStateOf(1f) }
    val darkModeColorGreen = if (isSystemInDarkTheme()) DarkModeGreen else Color(0xFF045C3C)

    LaunchedEffect(imageUrl) {
        imageUrl?.let {
            imageBitmap = loadImageBitmap(it, lastModified)
            imageBitmap?.let { bitmap ->
                imageAspectRatio = bitmap.width.toFloat() / bitmap.height.toFloat()
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
                                    offsetX += pan.x* 3.0f
                                    offsetY += pan.y* 3.0f
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
                Button(onClick = onClose, colors = ButtonDefaults.buttonColors(containerColor = darkModeColorGreen)) {
                    Text(text = "Fermer", color = Color.White)
                }
            }
            Spacer(modifier = Modifier.weight(0.1f))
        }
    }
}
