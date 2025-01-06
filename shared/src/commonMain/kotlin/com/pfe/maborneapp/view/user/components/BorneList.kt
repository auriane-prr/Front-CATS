package com.pfe.maborneapp.view.user.components

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
fun BorneList(bornes: List<Borne>, modifier: Modifier = Modifier) {
    // État pour suivre la borne sélectionnée et afficher la modale
    var selectedBorne by remember { mutableStateOf<Borne?>(null) }

    Text(
        text = "Bornes :",
        style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
        modifier = modifier
            .padding(horizontal = 16.dp)
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

        // Liste des bornes
        bornes.forEachIndexed { index, borne ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { selectedBorne = borne } // Rendre l'élément cliquable
                    .padding(vertical = 8.dp), // Padding vertical pour chaque ligne
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Pastille de couleur
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(
                            color = when (borne.status) {
                                "Disponible" -> Color(0xFF045C3C) // Vert foncé
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
                    style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f) // Prend tout l'espace disponible sauf le chevron
                )

                // Chevron à la fin
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Chevron",
                    tint = Color(0xFF045C3C) // Vert foncé pour correspondre au style
                )
            }

            // Ajouter une ligne de séparation sauf après le dernier élément
            if (index < bornes.size - 1) {
                Divider(
                    color = Color(0xFF045C3C).copy(alpha = 0.2f), // Ligne de séparation légèrement transparente
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }

    if (selectedBorne != null) {
        BorneModal(
            selectedBorne = selectedBorne,
            onClose = { selectedBorne = null }
        )
    }

}
