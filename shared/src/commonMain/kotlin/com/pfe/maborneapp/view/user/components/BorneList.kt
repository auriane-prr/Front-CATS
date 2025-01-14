package com.pfe.maborneapp.view.user.components

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
import androidx.compose.ui.unit.sp
import com.pfe.maborneapp.models.*
import com.pfe.maborneapp.utils.DarkModeGreen
import com.pfe.maborneapp.viewmodel.BorneViewModel
import com.pfe.maborneapp.viewmodel.SignalementViewModel

@Composable
fun BorneList(
    etatBornes: EtatBornes,
    userId: String,
    signalementViewModel: SignalementViewModel,
    borneViewModel: BorneViewModel,
    showAlert: (String, Boolean) -> Unit,
    containerColor: Color,
    modifier: Modifier = Modifier
) {
    var selectedBorne by remember { mutableStateOf<Borne?>(null) }

    val darkModeColorTitle = if (isSystemInDarkTheme()) DarkModeGreen else Color(0xFF045C3C)

    val allBornes = listOf(
        etatBornes.disponible.map { it to "Disponible" },
        etatBornes.occupee.map { it to "Occupée" },
        etatBornes.hs.map { it to "Hors Service" },
        etatBornes.signalee.map { it to "Signalée" }
    ).flatten()


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
            allBornes.forEachIndexed { index, (borne, label) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            when (label) {
                                "Signalée", "Hors Service" -> showAlert("Cette borne est momentanément indisponible.", false)
                                "Occupée" -> showAlert("Cette borne est déjà occupée.", false)
                                "Disponible" -> selectedBorne = borne
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

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = "Borne ${borne.numero} - $label",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f),
                        fontSize = 16.sp
                    )

                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Chevron",
                        tint = darkModeColorTitle
                    )
                }

                if (index < allBornes.size - 1) {
                    Divider(
                        color = darkModeColorTitle.copy(alpha = 0.2f),
                        thickness = 1.dp
                    )
                }
            }
        }


    if (selectedBorne != null) {
        SignalementModal(
            selectedBorne = selectedBorne,
            userId = userId,
            signalementViewModel = signalementViewModel,
            onClose = { selectedBorne = null },
            onRefreshBornes = { borneViewModel.fetchBornesByEtat() },
            showAlert = showAlert
        )
    }
}
