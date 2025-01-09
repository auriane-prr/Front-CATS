package com.pfe.maborneapp.view.user.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pfe.maborneapp.models.Reservation
import com.pfe.maborneapp.utils.formatDateTime

@Composable
fun ReservationCard(reservation: Reservation) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(
                color = Color(0xFFBDD3D0),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "${reservation.borne.carte.nom}     -     Borne ${reservation.borne.numero} ",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "du ${formatDateTime(reservation.dateDebut)} au ${formatDateTime(reservation.dateFin)} ",
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