package com.pfe.maborneapp.view.user.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pfe.maborneapp.models.Borne
import com.pfe.maborneapp.models.Reservation
import com.pfe.maborneapp.models.User
import com.pfe.maborneapp.utils.DarkModeGreen
import com.pfe.maborneapp.viewmodel.user.ReservationViewModel

@Composable
fun ReservationModal(
    reservationViewModel: ReservationViewModel,
    userId: String,
    onClose: () -> Unit,
    onReservationSuccess: () -> Unit
) {
    var selectedDate by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }
    val availableBornes by reservationViewModel.availableBornes.collectAsState()
    var selectedBorne by remember { mutableStateOf<Borne?>(null) }
    val creationStatus by reservationViewModel.creationStatus.collectAsState()

    val darkModeColorGreen = if (isSystemInDarkTheme()) DarkModeGreen else Color(0xFF045C3C)

    AlertDialog(
        onDismissRequest = { onClose() },
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Nouvelle réservation",
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }
        },
        text = {
            Column {
                // Sélecteur de date
                OutlinedTextField(
                    value = selectedDate,
                    onValueChange = { selectedDate = it },
                    label = { Text("Sélectionnez une date (JJ-MM-AAAA)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Sélecteur d'heure début
                OutlinedTextField(
                    value = startTime,
                    onValueChange = { startTime = it },
                    label = { Text("Heure de début (HH:mm)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Sélecteur d'heure fin
                OutlinedTextField(
                    value = endTime,
                    onValueChange = { endTime = it },
                    label = { Text("Heure de fin (HH:mm)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Bouton pour récupérer les bornes disponibles
                Button(
                    onClick = {
                        // Convertir la date et l'heure dans le format attendu par l'API
                        val start = "${selectedDate.split("-").reversed().joinToString("-")}T${startTime}:00"
                        val end = "${selectedDate.split("-").reversed().joinToString("-")}T${endTime}:00"

                        println("DEBUG: start = $start, end = $end")
                        reservationViewModel.fetchAvailableBornes(start, end)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = darkModeColorGreen)
                ) {
                    Text(text = "Afficher les bornes disponibles", color = Color.White)
                }


                // Liste des bornes disponibles
                if (availableBornes != null && availableBornes!!.isNotEmpty()) {
                    Text(
                        text = "Bornes disponibles :",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    availableBornes!!.forEach { borne ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedBorne = borne }
                                .padding(vertical = 4.dp)
                                .background(
                                    color = if (selectedBorne == borne) darkModeColorGreen else Color(0xFFE0E0E0),
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Borne ${borne.numero} - ${borne.status}",
                                color = if (selectedBorne == borne) Color.White else Color.Black
                            )
                        }
                    }
                } else {
                    Text(
                        text = "Aucune borne disponible.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    selectedBorne?.let { borne ->
                        val formattedStartDate = "${selectedDate.split("-").reversed().joinToString("-")}T${startTime}:00"
                        val formattedEndDate = "${selectedDate.split("-").reversed().joinToString("-")}T${endTime}:00"

                        println("DEBUG: Date début formatée = $formattedStartDate, Date fin formatée = $formattedEndDate")

                        val reservation = Reservation(
                            id = "", // Généré par le backend
                            borne = borne,
                            user = User(userId, "user@example.com", "User"), // Utilisateur actuel
                            dateDebut = formattedStartDate,
                            dateFin = formattedEndDate
                        )
                        reservationViewModel.createReservation(reservation)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = darkModeColorGreen)
            ) {
                Text(text = "Confirmer", color = Color.White)
            }
        },
        dismissButton = {
            Button(onClick = onClose) {
                Text(text = "Annuler")
            }
        }
    )

    // Gestion de la création de réservation
    LaunchedEffect(creationStatus) {
        if (creationStatus == true) {
            onReservationSuccess()
            onClose()
        }
    }
}
