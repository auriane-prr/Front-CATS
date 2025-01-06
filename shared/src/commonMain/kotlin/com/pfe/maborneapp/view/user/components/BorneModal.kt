package com.pfe.maborneapp.view.user.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pfe.maborneapp.models.Borne

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BorneModal(selectedBorne: Borne?, onClose: () -> Unit) {
    if (selectedBorne != null) {
        var activeTab by remember { mutableStateOf("Réserver") }
        var reservationDate by remember { mutableStateOf("") }
        var reservationTime by remember { mutableStateOf("") }
        var reportReason by remember { mutableStateOf(TextFieldValue("")) }

        AlertDialog(
            onDismissRequest = { onClose() },
            title = {
                TabRow(
                    selectedTabIndex = if (activeTab == "Réserver") 0 else 1,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Tab(
                        selected = activeTab == "Réserver",
                        onClick = { activeTab = "Réserver" },
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = "Réserver",
                            color = if (activeTab == "Réserver") Color(0xFF045C3C) else Color.Gray
                        )
                    }
                    Tab(
                        selected = activeTab == "Signaler",
                        onClick = { activeTab = "Signaler" },
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = "Signaler",
                            color = if (activeTab == "Signaler") Color(0xFF045C3C) else Color.Gray
                        )
                    }
                }
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                    if (activeTab == "Réserver") {
                        // Contenu de l'onglet Réservation
                        Text(
                            text = "Réserver la borne ${selectedBorne.numero}",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color(0xFF045C3C)
                        )
                        Divider(
                            color = Color(0xFF045C3C),
                            modifier = Modifier.padding(vertical = 8.dp),
                            thickness = 1.dp
                        )
                        Text(text = "Date", fontSize = 14.sp)
                        OutlinedTextField(
                            value = reservationDate,
                            onValueChange = { reservationDate = it },
                            placeholder = { Text("Choisissez une date") },
                            trailingIcon = { Text("V", color = Color.Gray) },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                        )
                        Text(text = "Horaire", fontSize = 14.sp)
                        OutlinedTextField(
                            value = reservationTime,
                            onValueChange = { reservationTime = it },
                            placeholder = { Text("Choisissez un horaire") },
                            trailingIcon = { Text("V", color = Color.Gray) },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                        )
                    } else {
                        // Contenu de l'onglet Signalement
                        Text(
                            text = "Signaler la borne ${selectedBorne.numero}",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color(0xFF045C3C)
                        )
                        Divider(
                            color = Color(0xFF045C3C),
                            modifier = Modifier.padding(vertical = 8.dp),
                            thickness = 1.dp
                        )
                        Text(text = "Pourquoi signalez-vous cette borne ?", fontSize = 14.sp)
                        OutlinedTextField(
                            value = reportReason,
                            onValueChange = { reportReason = it },
                            placeholder = { Text("Expliquez ici...") },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .padding(vertical = 8.dp)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { onClose() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD9E6E1)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Confirmer", color = Color(0xFF045C3C))
                }
            },
            dismissButton = null
        )
    }
}
