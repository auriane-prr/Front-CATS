package com.pfe.maborneapp.view.admin.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pfe.maborneapp.models.Signalement
import com.pfe.maborneapp.utils.*

@Composable
fun SignalementList(
    signalements: List<Signalement>,
    modifier: Modifier = Modifier,
    onUpdateStatus: (borneId: String, newStatus: String, signalementId: String?) -> Unit
) {
    var selectedSignalement by remember { mutableStateOf<Signalement?>(null) }

    val darkModeBoackground = if (isSystemInDarkTheme()) DarkContainerColor else MaterialTheme.colorScheme.surface
    val darkModeColorGreen = if (isSystemInDarkTheme()) DarkModeGreen else Color(0xFF045C3C)

    Text(
        text = "Signalements actifs :",
        style = MaterialTheme.typography.titleMedium,
        modifier = modifier.padding(horizontal = 16.dp)
    )

    Column(
        modifier = modifier
            .padding(16.dp)
            .background(
                color = darkModeBoackground,
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 2.dp,
                color = darkModeColorGreen,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        if (signalements.isEmpty()) {
            Text(
                text = "Aucun signalement actif disponible.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 10.dp)
            )
        } else {
            signalements.forEachIndexed { index, signalement ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedSignalement = signalement }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(14.dp)
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
                    Text(
                        text = "Borne ${signalement.borne.numero} - ${signalement.motif}",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.weight(1f)
                    )
                }
                if (index < signalements.size - 1) {
                    Divider(
                        color = darkModeColorGreen.copy(alpha = 0.2f),
                        thickness = 1.dp,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
    }

    if (selectedSignalement != null) {
        SignalementPopup(
            signalement = selectedSignalement!!,
            onDismiss = { selectedSignalement = null },
            onUpdateStatus = onUpdateStatus
        )
    }
}
