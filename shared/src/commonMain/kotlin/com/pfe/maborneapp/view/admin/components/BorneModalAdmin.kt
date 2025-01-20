package com.pfe.maborneapp.view.admin.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pfe.maborneapp.models.Borne
import com.pfe.maborneapp.utils.DarkModeGreen
import com.pfe.maborneapp.view.components.Alert

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BorneModalAdmin(
    selectedBorne: Borne?,
    onClose: () -> Unit,
    onUpdateStatus: (borneId: String, newStatus: String) -> Unit,
    onDelete: (String) -> Unit
) {
    val darkModeColorGreen = if (isSystemInDarkTheme()) DarkModeGreen else Color(0xFF045C3C)

    // États pour afficher les alertes de confirmation
    var showConfirmationDialog by remember { mutableStateOf(false) }
    var actionType by remember { mutableStateOf("") }

    if (selectedBorne != null) {
        AlertDialog(
            onDismissRequest = { onClose() },
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Détails de la borne",
                        style = MaterialTheme.typography.titleLarge,
                        color = darkModeColorGreen,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = { onClose() }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Fermer",
                            tint = darkModeColorGreen
                        )
                    }
                }
            },
            text = {
                Column {
                    Text("Borne : ${selectedBorne.numero}", fontSize = 16.sp)
                    Text("Statut : ${selectedBorne.status}", fontSize = 16.sp)

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = {
                                actionType = "Fonctionnelle"
                                showConfirmationDialog = true
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF045C3C)),
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Déclarer Fonctionnelle", color = Color.White, fontSize = 13.sp)
                        }
                        Button(
                            onClick = {
                                actionType = "hs"
                                showConfirmationDialog = true
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBDBDBD)),
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Déclarer HS", color = Color.White, fontSize = 13.sp)
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                Button(
                    onClick = {
                        actionType = "delete"
                        showConfirmationDialog = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Supprimer")
                }
            }
        )
    }

    // Affichage de la confirmation avec les boutons "Oui" et "Annuler"
    if (showConfirmationDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmationDialog = false },
            title = {
                Text(
                    text = "Confirmation",
                    style = MaterialTheme.typography.titleLarge,
                    color = darkModeColorGreen
                )
            },
            text = {
                Text(
                    text = when (actionType) {
                        "delete" -> "Voulez-vous vraiment supprimer cette borne ?"
                        "Fonctionnelle" -> "Voulez-vous déclarer cette borne comme Fonctionnelle ?"
                        "hs" -> "Voulez-vous déclarer cette borne comme Hors Service ?"
                        else -> ""
                    },
                    fontSize = 16.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        selectedBorne?.id?.let { id ->
                            if (actionType == "delete") {
                                onDelete(id)
                            } else {
                                onUpdateStatus(id, actionType)
                            }
                        }
                        showConfirmationDialog = false
                        onClose()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = darkModeColorGreen)
                ) {
                    Text("Oui", color = Color.White)
                }
            },
            dismissButton = {
                Button(
                    onClick = { showConfirmationDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text("Annuler")
                }
            }
        )
    }
}
