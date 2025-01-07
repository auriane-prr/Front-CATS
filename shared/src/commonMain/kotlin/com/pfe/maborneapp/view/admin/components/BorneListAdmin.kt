package com.pfe.maborneapp.view.admin.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pfe.maborneapp.models.Borne

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BorneListAdmin(bornes: List<Borne>, modifier: Modifier = Modifier) {
    // État pour suivre la borne sélectionnée et afficher la modale
    var selectedBorne by remember { mutableStateOf<Borne?>(null) }

    Text(
        text = "Bornes (Admin) :",
        style = MaterialTheme.typography.titleMedium,
        modifier = modifier
            .padding(horizontal = 16.dp)
    )

    Column(
        modifier = modifier
            .padding(16.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 2.dp,
                color = Color(0xFF045C3C),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {

        Spacer(modifier = Modifier.height(8.dp))

        // Liste des bornes
        bornes.forEachIndexed { index, borne ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { selectedBorne = borne }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Pastille de couleur
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(
                            color = when (borne.status) {
                                "Disponible" -> Color(0xFF045C3C) // Vert
                                "Occupée" -> Color.Red
                                "HS" -> Color.Gray
                                "Signalée" -> Color(0xFFFFB512) // Jaune-orange
                                else -> Color.Transparent
                            },
                            shape = CircleShape
                        )
                )
                Spacer(modifier = Modifier.width(8.dp))

                // Texte du statut
                Text(
                    text = "Borne ${borne.numero} - ${borne.status}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f)
                )

                // Chevron
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Chevron",
                    tint = Color(0xFF045C3C)
                )
            }

            if (index < bornes.size - 1) {
                Divider(
                    color = Color(0xFF045C3C).copy(alpha = 0.2f),
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }

    if (selectedBorne != null) {
        BorneModalAdmin(
            selectedBorne = selectedBorne,
            onClose = { selectedBorne = null }
        )
    }
}
