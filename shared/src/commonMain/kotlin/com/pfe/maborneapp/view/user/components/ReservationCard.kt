package com.pfe.maborneapp.view.user.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pfe.maborneapp.models.Reservation
import com.pfe.maborneapp.utils.formatDateOnly
import com.pfe.maborneapp.utils.formatTimeOnly
import com.pfe.maborneapp.utils.DarkContainerColor
import com.pfe.maborneapp.utils.DarkModeGreen
import com.pfe.maborneapp.view.components.Alert

@Composable
fun ReservationCard(reservation: Reservation, onDelete: ((String) -> Unit)?) {

    val backgroundColor = if (isSystemInDarkTheme()) DarkContainerColor else Color(0xFFBDD3D0)
    val darkModeColorGreen = if (isSystemInDarkTheme()) DarkModeGreen else Color(0xFF045C3C)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            val startDate = formatDateOnly(reservation.dateDebut)
            val startTime = formatTimeOnly(reservation.dateDebut)
            val endTime = formatTimeOnly(reservation.dateFin)

            Text(
                text = "$startDate, $startTime - $endTime",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "Borne ${reservation.borne.numero} - ${reservation.borne.carte.nom}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        if (onDelete != null) {
            IconButton(onClick = { onDelete(reservation.id) }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Supprimer la réservation",
                    tint = darkModeColorGreen
                )
            }
        }
    }
}
