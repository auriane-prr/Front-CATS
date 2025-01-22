package com.pfe.maborneapp.view.admin.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pfe.maborneapp.models.Signalement
import com.pfe.maborneapp.utils.*

@Composable
fun SignalementPopup(
    signalement: Signalement,
    onDismiss: () -> Unit,
    onUpdateStatus: (borneId: String, newStatus: String, signalementId: String?) -> Unit
) {
    val darkModeColorGreen = if (isSystemInDarkTheme()) DarkModeGreen else Color(0xFF045C3C)
    val darkModeBoackground = if (isSystemInDarkTheme()) DarkContainerColor else MaterialTheme.colorScheme.surface

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Détails du signalement",
                    style = MaterialTheme.typography.titleLarge,
                    color = darkModeColorGreen,
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = { onDismiss() },
                    modifier = Modifier
                        .size(30.dp)
                        .padding(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Fermer",
                        tint = darkModeColorGreen,
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DetailItem(label = "Borne", value = signalement.borne.numero.toString())
                DetailItem(label = "Motif", value = signalement.motif)
                DetailItem(label = "Utilisateur", value = signalement.user.mail)
                DetailItem(label = "Type de borne", value = signalement.borne.typeBorne.nom)
                DetailItem(label = "Carte", value = signalement.borne.carte.nom)

                Divider(color = Color(0xFFE0E0E0))
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 1.dp)
                ) {
                    Button(
                        onClick = {
                            signalement.borne.id?.let { borneId ->
                                onUpdateStatus(borneId, "hs", signalement.id)
                                onDismiss()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Déclarer HS",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Button(
                        onClick = {
                            signalement.borne.id?.let { borneId ->
                                onUpdateStatus(borneId, "Fonctionnelle", signalement.id)
                                onDismiss()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = darkModeColorGreen),
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Déclarer Fonctionnelle",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {},
        shape = RoundedCornerShape(16.dp),
        containerColor = darkModeBoackground,
    )
}


@Composable
fun DetailItem(label: String, value: String) {
    val darkModeTitle = if (isSystemInDarkTheme()) Color.White else Color(0xFF616161)
    val darkModeValeur = if (isSystemInDarkTheme()) DarkModeGreen else Color(0xFF212121)
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$label :",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = darkModeTitle
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = darkModeValeur
        )
    }
}
