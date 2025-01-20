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
import com.pfe.maborneapp.viewmodel.BorneViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BorneListAdmin(
    etatBornes: EtatBornes,
    selectedCarteId: String,
    viewModel: BorneViewModel,
    modifier: Modifier = Modifier,
    containerColor: Color,
) {
    var selectedBorne by remember { mutableStateOf<Borne?>(null) }
    var showSignalementAlert by remember { mutableStateOf(false) } // Gérer l'affichage de l'alerte pour les bornes signalées

    val darkModeColorTitle = if (isSystemInDarkTheme()) DarkModeGreen else Color(0xFF045C3C)

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

        val filteredBornes = listOf(
            etatBornes.disponible.filter { it.carte.id == selectedCarteId }.map { it to "Disponible" },
            etatBornes.occupee.filter { it.carte.id == selectedCarteId }.map { it to "Occupée" },
            etatBornes.hs.filter { it.carte.id == selectedCarteId }.map { it to "Hors Service" },
            etatBornes.signalee.filter { it.carte.id == selectedCarteId }.map { it to "Signalée" }
        ).flatten()

        filteredBornes.forEachIndexed { index, (borne, label) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        if (label == "Signalée") {
                            showSignalementAlert = true  // Afficher l'alerte si la borne est signalée
                        } else {
                            selectedBorne = borne  // Sélectionner la borne pour gestion si non signalée
                        }
                    }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
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

                Text(
                    text = "Borne ${borne.numero} - $label",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Chevron",
                    tint = darkModeColorTitle
                )
            }

            if (index < filteredBornes.size - 1) {
                Divider(
                    color = darkModeColorTitle.copy(alpha = 0.2f),
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }

    // Gestion de l'alerte pour les bornes signalées
    if (showSignalementAlert) {
        AlertDialog(
            onDismissRequest = { showSignalementAlert = false },
            title = { Text("Attention") },
            text = { Text("Cette borne est actuellement signalée. Veuillez vous rendre sur la page de signalement pour plus d'informations.") },
            confirmButton = {
                Button(onClick = { showSignalementAlert = false },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF045C3C)),) {
                    Text("OK")
                }
            }
        )
    }

    // Modal pour la gestion des bornes non signalées
    selectedBorne?.let { borne ->
        BorneModalAdmin(
            selectedBorne = borne,
            onClose = { selectedBorne = null },
            onDelete = { id ->
                viewModel.deleteBorne(id)
            },
            onUpdateStatus = { borneId, newStatus ->
                viewModel.updateBorneStatus(
                    borneId,
                    newStatus,
                    onSuccess = { println("DEBUG: Statut mis à jour avec succès pour la borne $borneId") },
                    onError = { error -> println("DEBUG: Erreur lors de la mise à jour du statut : $error") }
                )
                selectedBorne = null  // Fermer la popup après mise à jour
            }
        )
    }
}
