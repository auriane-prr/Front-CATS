package com.pfe.maborneapp.view.admin.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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

@Composable
fun SignalementPopup(
    signalement: Signalement,
    onDismiss: () -> Unit,
    onUpdateStatus: (borneId: String, newStatus: String) -> Unit // Callback pour changer l'état
) {
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
                    color = Color(0xFF045C3C),
                    modifier = Modifier.weight(1f) // Prend tout l'espace disponible sauf pour l'icône
                )

                IconButton(
                    onClick = { onDismiss() },
                    modifier = Modifier
                        .size(24.dp) // Taille discrète
                        .padding(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Fermer",
                        tint = Color(0xFF045C3C) // Même couleur que le bouton "Déclarer Fonctionnelle"
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
                // Boutons pour changer l'état de la borne
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp), // Augmente l'espacement entre les boutons
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 1.dp) // Ajoute une marge pour que les boutons ne touchent pas les bords
                ) {
                    Button(
                        onClick = {
                            signalement.borne.id?.let { id ->
                                onUpdateStatus(id, "hs")
                                onDismiss() // Ferme le popup après l'action
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBDBDBD)),
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp), // Augmente la hauteur pour mieux afficher le texte
                        shape = RoundedCornerShape(12.dp) // Courbe légèrement plus marquée
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center // Centrage du texte
                        ) {
                            Text(
                                "Déclarer HS",
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Button(
                        onClick = {
                            signalement.borne.id?.let { id ->
                                onUpdateStatus(id, "Fonctionnelle")
                                onDismiss() // Ferme le popup après l'action
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF045C3C)), // Même couleur que la croix
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp), // Même hauteur que l'autre bouton
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center // Centrage du texte
                        ) {
                            Text(
                                "Déclarer Fonctionnelle",
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {}, // Supprime le bouton "Fermer"
        shape = RoundedCornerShape(16.dp),
        containerColor = Color(0xFFF5F5F5)
    )
}

@Composable
fun DetailItem(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$label :",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = Color(0xFF616161)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF212121)
        )
    }
}
