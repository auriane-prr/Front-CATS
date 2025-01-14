package com.pfe.maborneapp.view.admin.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pfe.maborneapp.models.Borne
import com.pfe.maborneapp.utils.DarkModeGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BorneModalAdmin(selectedBorne: Borne?, onClose: () -> Unit) {
    val darkModeColorGreen = if (isSystemInDarkTheme()) DarkModeGreen else Color(0xFF045C3C)

    if (selectedBorne != null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    renderEffect = BlurEffect(20f, 20f) // Flou sur tout l'arrière-plan
                }
                .background(Color.Black.copy(alpha = 0.4f)), // Couleur et opacité de la Box
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .background(Color.White, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                AlertDialog(
                    onDismissRequest = { onClose() },
                    modifier = Modifier
                        .fillMaxWidth(),
                    title = {
                        Text(
                            text = "Détails de la borne",
                            style = MaterialTheme.typography.titleLarge,
                            color = darkModeColorGreen,
                            modifier = Modifier.padding(16.dp)
                        )
                    },
                    text = {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "Borne : ${selectedBorne.numero}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = darkModeColorGreen
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Statut : ${selectedBorne.status}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )

                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = { onClose() },
                            colors = ButtonDefaults.buttonColors(containerColor = darkModeColorGreen),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Fermer" )
                        }
                    },
                    dismissButton = null
                )
            }
        }
    }
}
