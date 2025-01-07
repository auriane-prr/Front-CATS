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
import com.pfe.maborneapp.models.Signalement

@Composable
fun SignalementList(signalements: List<Signalement>, modifier: Modifier = Modifier) {
    // État pour suivre le signalement sélectionné et afficher la popup
    var selectedSignalement by remember { mutableStateOf<Signalement?>(null) }

    Text(
        text = "Signalements :",
        style = MaterialTheme.typography.titleMedium,
        modifier = modifier.padding(horizontal = 16.dp)
    )

    Column(
        modifier = modifier
            .padding(16.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(16.dp) // Coins arrondis
            )
            .border(
                width = 2.dp,
                color = Color(0xFF045C3C), // Contour vert
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp) // Padding interne
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // Liste des signalements
        signalements.forEachIndexed { index, signalement ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { selectedSignalement = signalement } // Rendre cliquable pour afficher les détails
                    .padding(vertical = 8.dp), // Padding vertical pour chaque ligne
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Pastille de couleur pour le statut
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(
                            color = when (signalement.borne.status) {
                                "Signalée" -> Color(0xFFFFB512) // Jaune-orange
                                "HS" -> Color.Gray // Hors service
                                "Disponible" -> Color(0xFF045C3C) // Disponible (vert foncé)
                                "Occupée" -> Color.Red // Occupée (rouge)
                                else -> Color.Transparent
                            },
                            shape = CircleShape
                        )
                )
                Spacer(modifier = Modifier.width(8.dp))

                // Texte principal avec le motif
                Text(
                    text = "Borne ${signalement.borne.numero} - ${signalement.motif}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f) // Prend tout l'espace disponible sauf l'icône
                )

                // Chevron pour indiquer plus de détails
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Détails",
                    tint = Color(0xFF045C3C) // Vert foncé pour correspondre au style
                )
            }

            // Ligne de séparation sauf après le dernier élément
            if (index < signalements.size - 1) {
                Divider(
                    color = Color(0xFF045C3C).copy(alpha = 0.2f), // Ligne de séparation légèrement transparente
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }

    // Popup pour afficher les détails d'un signalement
    if (selectedSignalement != null) {
        SignalementPopup(
            signalement = selectedSignalement!!,
            onDismiss = { selectedSignalement = null }
        )
    }
}
