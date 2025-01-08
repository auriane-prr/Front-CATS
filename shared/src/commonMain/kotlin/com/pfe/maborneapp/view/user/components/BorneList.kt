package com.pfe.maborneapp.view.user.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pfe.maborneapp.models.*
import com.pfe.maborneapp.view.components.Alert
import com.pfe.maborneapp.viewmodel.SignalementViewModel

@Composable
fun BorneList(
    etatBornes: EtatBornes,
    userId: String,
    signalementViewModel: SignalementViewModel,
    modifier: Modifier = Modifier
) {
    var selectedBorne by remember { mutableStateOf<Borne?>(null) }
    var showAlert by remember { mutableStateOf(false) }
    var alertMessage by remember { mutableStateOf("") }

    // Combine toutes les bornes en une seule liste
    val allBornes = listOf(
        etatBornes.disponible.map { it to "Disponible" },
        etatBornes.occupee.map { it to "Occupée" },
        etatBornes.hs.map { it to "Hors Service" },
        etatBornes.signalee.map { it to "Signalée" }
    ).flatten()

    // Texte du titre
    Text(
        text = "Bornes :",
        style = MaterialTheme.typography.titleMedium,
        modifier = modifier.padding(horizontal = 16.dp)
    )

    // Conteneur défilable
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()) // Activation du défilement
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

        // Afficher chaque borne
        allBornes.forEachIndexed { index, (borne, label) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        if (label == "Hors Service" || label == "Signalée") {
                            alertMessage =
                                "Désolé, cette borne est momentanément indisponible, vous ne pouvez pas la réserver ni la signaler."
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

                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Chevron",
                    tint = Color(0xFF045C3C)
                )
            }

            if (index < allBornes.size - 1) {
                Divider(
                    color = Color(0xFF045C3C).copy(alpha = 0.2f),
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }

    // Modale de borne
    if (selectedBorne != null) {
        BorneModal(
            selectedBorne = selectedBorne,
            userId = userId,
            signalementViewModel = signalementViewModel,
            onClose = { selectedBorne = null }
        )
    }

    // Alerte d'indisponibilité
    if (showAlert) {
        Alert(
            show = true,
            isSuccess = false,
            message = alertMessage,
            onDismiss = { showAlert = false }
        )
    }
}
