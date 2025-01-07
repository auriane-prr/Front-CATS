package com.pfe.maborneapp.view.admin.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pfe.maborneapp.models.Signalement

@Composable
fun SignalementPopup(signalement: Signalement, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                text = "Détails du signalement",
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFF045C3C)
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = "Borne : ${signalement.borne.numero}")
                Text(text = "Motif : ${signalement.motif}")
                Text(text = "Date : ${signalement.date}")
                Text(text = "Utilisateur : ${signalement.user.mail}")
                Text(text = "Coordonnées : (${signalement.borne.coord_x}, ${signalement.borne.coord_y})")
                Text(text = "Type de borne : ${signalement.borne.typeBorne.nom}")
                Text(text = "Carte : ${signalement.borne.carte.nom}")
            }
        },
        confirmButton = {
            Button(onClick = { onDismiss() }) {
                Text(text = "Fermer")
            }
        },
        shape = RoundedCornerShape(16.dp),
        containerColor = Color.White
    )
}
