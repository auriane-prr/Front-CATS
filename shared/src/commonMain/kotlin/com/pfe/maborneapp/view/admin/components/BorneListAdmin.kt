package com.pfe.maborneapp.view.admin.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import com.pfe.maborneapp.models.EtatBornes
import com.pfe.maborneapp.utils.DarkModeGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BorneListAdmin(etatBornes: EtatBornes, modifier: Modifier = Modifier,containerColor: Color,) {
    var selectedBorne by remember { mutableStateOf<Borne?>(null) }
    val darkModeColorTitle = if (isSystemInDarkTheme()) DarkModeGreen else Color(0xFF045C3C)

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
                color = containerColor,
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 2.dp,
                color = darkModeColorTitle,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // Combine toutes les bornes en une seule liste avec des libellés adaptés
        val allBornes = listOf(
            etatBornes.disponible.map { it to "Disponible" },
            etatBornes.occupee.map { it to "Occupée" },
            etatBornes.hs.map { it to "Hors Service" },
            etatBornes.signalee.map { it to "Signalée" }
        ).flatten()

        // Afficher chaque borne
        allBornes.forEachIndexed { index, (borne, label) ->
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
                            color = when (label) {
                                "Disponible" -> Color(0xFF37A756)
                                "Occupée" -> Color.Red
                                "Hors Service" -> Color.Gray
                                "Signalée" -> Color(0xFFFFB512)
                                else -> Color.Transparent
                            },
                            shape = CircleShape
                        )
                )
                Spacer(modifier = Modifier.width(8.dp))

                // Texte du statut et numéro de borne
                Text(
                    text = "Borne ${borne.numero} - $label",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f)
                )

                // Chevron
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Chevron",
                    tint = darkModeColorTitle
                )
            }

            if (index < allBornes.size - 1) {
                Divider(
                    color = darkModeColorTitle.copy(alpha = 0.2f),
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
