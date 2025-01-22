package com.pfe.maborneapp.view.components.image

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MapView(
    imageUrl: String?,
    lastModified: String?,
    contentDescription: String
) {
    var showZoomableImage by remember { mutableStateOf(false) }

    if (showZoomableImage) {
        ZoomableImageView(
            imageUrl = imageUrl,
            lastModified = lastModified,
            contentDescription = contentDescription,
            onClose = { showZoomableImage = false }
        )
    } else {
        NetworkImage(
            imageUrl = imageUrl,
            //lastModified = lastModified,
            contentDescription = contentDescription,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clickable { showZoomableImage = true } // Active le zoom au clic
        )
    }
}
