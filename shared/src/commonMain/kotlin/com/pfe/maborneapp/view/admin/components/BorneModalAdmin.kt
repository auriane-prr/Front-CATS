package com.pfe.maborneapp.view.admin.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pfe.maborneapp.models.Borne

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BorneModalAdmin(selectedBorne: Borne?, onClose: () -> Unit) {
    if (selectedBorne != null) {
        AlertDialog(
            onDismissRequest = { onClose() },
            title = {
                Text(
                    text = "Détails de la borne",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF045C3C),
                    modifier = Modifier.padding(16.dp)
                )
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                    Text(
                        text = "Borne : ${selectedBorne.numero}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF045C3C)
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
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD9E6E1)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Fermer", color = Color(0xFF045C3C))
                }
            },
            dismissButton = null
        )
    }
}
