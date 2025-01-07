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
import com.pfe.maborneapp.view.components.Alert
import com.pfe.maborneapp.viewmodel.user.SignalementViewModel

@Composable
fun BorneList(
    bornes: List<Borne>,
    userId: String,
    signalementViewModel: SignalementViewModel,
    modifier: Modifier = Modifier
) {
    var selectedBorne by remember { mutableStateOf<Borne?>(null) }
    var showAlert by remember { mutableStateOf(false) } // État pour afficher l'alerte
    var alertMessage by remember { mutableStateOf("") }

    Text(
        text = "Bornes :",
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

        bornes.forEachIndexed { index, borne ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        if (borne.status == "HS" || borne.status == "Signalée") {
                            alertMessage = "Désolé, cette borne est momentanément indisponible, vous ne pouvez pas la réserver ni la signaler."
                            showAlert = true
                        } else {
                            selectedBorne = borne
                        }
                    }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Pastille de couleur
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(
                            color = when (borne.status) {
                                "Disponible" -> Color(0xFF045C3C)
                                "Occupée" -> Color.Red
                                "HS" -> Color.Gray
                                "Signalée" -> Color(0xFFFFB512)
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

    // Affichage de la modale si une borne est sélectionnée
    if (selectedBorne != null) {
        BorneModal(
            selectedBorne = selectedBorne,
            userId = userId,
            signalementViewModel = signalementViewModel,
            onClose = { selectedBorne = null }
        )
    }

    // Affichage de l'alerte si nécessaire
    if (showAlert) {
        Alert(
            show = true,
            isSuccess = false, // Une alerte d'erreur
            message = alertMessage,
            onDismiss = { showAlert = false }
        )
    }
}
