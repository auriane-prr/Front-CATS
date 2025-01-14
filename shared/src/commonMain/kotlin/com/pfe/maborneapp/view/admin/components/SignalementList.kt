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
fun SignalementList(
    signalements: List<Signalement>,
    modifier: Modifier = Modifier,
    onCloseSignalement: (signalementId: String) -> Unit, // Fonction pour fermer un signalement
    onUpdateStatus: (borneId: String, newStatus: String) -> Unit // Fonction pour mettre à jour le statut
) {
    // État pour suivre le signalement sélectionné et afficher la popup
    var selectedSignalement by remember { mutableStateOf<Signalement?>(null) }

    // Filtrer les signalements pour exclure ceux qui sont fermés
    val signalementsActifs = signalements.filter { it.etat != "Fermé" }

    Text(
        text = "Signalements actifs :",
        style = MaterialTheme.typography.titleMedium,
        modifier = modifier.padding(horizontal = 16.dp)
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

        if (signalementsActifs.isEmpty()) {
            Text(
                text = "Aucun signalement actif disponible.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        } else {
            // Liste des signalements
            signalementsActifs.forEachIndexed { index, signalement ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedSignalement = signalement }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Pastille de couleur pour le statut
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .background(
                                color = when (signalement.borne.status) {
                                    "Signalée" -> Color(0xFFFFB512)
                                    "HS" -> Color.Gray
                                    "Disponible" -> Color(0xFF045C3C)
                                    "Occupée" -> Color.Red
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
                        modifier = Modifier.weight(1f)
                    )

                    // Bouton pour fermer le signalement
                    Button(
                        onClick = {
                            signalement.id?.let { id ->
                                onCloseSignalement(id)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Text("Fermer", color = Color.White)
                    }
                }

                if (index < signalementsActifs.size - 1) {
                    Divider(
                        color = Color(0xFF045C3C).copy(alpha = 0.2f),
                        thickness = 1.dp,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
    }

    // Popup pour afficher les détails d'un signalement
    if (selectedSignalement != null) {
        SignalementPopup(
            signalement = selectedSignalement!!,
            onDismiss = { selectedSignalement = null },
            onUpdateStatus = onUpdateStatus // Passez le paramètre ici
        )
    }
}
