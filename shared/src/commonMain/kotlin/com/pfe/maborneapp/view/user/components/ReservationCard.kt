package com.pfe.maborneapp.view.user.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pfe.maborneapp.models.Reservation
import com.pfe.maborneapp.utils.formatDateOnly
import com.pfe.maborneapp.utils.formatTimeOnly
import com.pfe.maborneapp.utils.DarkContainerColor

@Composable
fun ReservationCard(reservation: Reservation) {
    val backgroundColor = if (isSystemInDarkTheme()) DarkContainerColor else Color(0xFFBDD3D0)
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
            Text(
                text = "Borne ${reservation.borne.numero}     -     ${reservation.borne.carte.nom}",
                style = MaterialTheme.typography.titleMedium
            )
            val startDate = formatDateOnly(reservation.dateDebut)
            val startTime = formatTimeOnly(reservation.dateDebut)
            val endTime = formatTimeOnly(reservation.dateFin)

            Text(
                text = "Le $startDate de $startTime à $endTime",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        /*Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = "Chevron",
            tint = Color(0xFF045C3C)
        )*/
    }
}
