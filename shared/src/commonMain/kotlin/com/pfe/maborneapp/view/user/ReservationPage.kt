package com.pfe.maborneapp.view.user

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pfe.maborneapp.utils.*
import com.pfe.maborneapp.view.user.components.Menu
import com.pfe.maborneapp.models.Reservation
import com.pfe.maborneapp.view.components.Alert
import com.pfe.maborneapp.view.user.components.ReservationCard
import com.pfe.maborneapp.viewmodel.UserViewModel
import com.pfe.maborneapp.viewmodel.user.*

@Composable
fun ReservationPage(
    navController: NavController,
    userViewModel: UserViewModel,
    reservationViewModel: ReservationViewModel
) {

    val userId by userViewModel.userId.collectAsState()
    val reservations by reservationViewModel.reservations.collectAsState()
    val upcomingReservations = remember { mutableStateListOf<Reservation>() }
    val pastReservations = remember { mutableStateListOf<Reservation>() }

    val isLoading by reservationViewModel.isLoading.collectAsState()

    // États pour gérer la pop-up, l'alerte et les actions
    var showDialog by remember { mutableStateOf(false) }
    var selectedReservationId by remember { mutableStateOf<String?>(null) }
    var showAlert by remember { mutableStateOf(false) }
    var alertMessage by remember { mutableStateOf("") }
    var isAlertSuccess by remember { mutableStateOf(false) }
    var isProcessingDelete by remember { mutableStateOf(false) }

    val deleteStatus by reservationViewModel.deleteStatus.collectAsState()

    // Charger les réservations à l'initialisation
    LaunchedEffect(userId) {
        userViewModel.fetchUserEmail(userId)
        reservationViewModel.fetchReservations(userId)
    }

    // Mettre à jour les listes triées lors de changements
    LaunchedEffect(reservations) {
        if (reservations != null) {
            upcomingReservations.clear()
            pastReservations.clear()
            val (upcoming, past) = reservationViewModel.getSortedReservations()
            upcomingReservations.addAll(upcoming)
            pastReservations.addAll(past)
        }
    }

    // Réagir aux changements d'état de suppression
    LaunchedEffect(deleteStatus) {
        deleteStatus?.let {
            if (it) {
                isAlertSuccess = true
                alertMessage = "Réservation annulée avec succès."
            } else {
                isAlertSuccess = false
                alertMessage = "Échec de l'annulation de la réservation."
            }
            showAlert = true // Afficher l'alerte
            reservationViewModel.resetDeleteStatus()
        }
    }


    var isMenuOpen by remember { mutableStateOf(false) }
    val darkModeColorGreen = if (isSystemInDarkTheme()) DarkModeGreen else Color(0xFF045C3C)

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigateAndClear("newReservation", reservationViewModel)
                },
                containerColor = darkModeColorGreen,
                contentColor = Color.White,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Nouvelle réservation")
            }
        },
        content = {
            val scrollState = rememberScrollState()
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                ) {

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text =  "Mes réservations",
                        style = MaterialTheme.typography.titleLarge,
                        color = darkModeColorGreen
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Prochaines réservations
                    Text(
                        text = "Prochaines réservations",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    if (isLoading) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = darkModeColorGreen)
                        }
                    } else {

                        if (upcomingReservations.isEmpty()) {
                            Text(text = "Aucune réservation à venir.")
                        } else {
                            upcomingReservations.forEach { reservation ->
                                ReservationCard(
                                    reservation = reservation,
                                    onDelete = { reservationId ->
                                        selectedReservationId = reservationId
                                        showDialog = true
                                    }
                                )
                            }
                        }

                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Séparateur historique
                    Divider(color = Color.Gray, thickness = 1.dp)

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = "Historique",
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Historique",
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }

                    if (isLoading) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = darkModeColorGreen)
                        }
                    } else {

                        // Réservations passées
                        if (pastReservations.isEmpty()) {
                            Text(text = "Aucune réservation passée.")
                        } else {
                            pastReservations.forEach { reservation ->
                                ReservationCard(
                                    reservation = reservation,
                                    onDelete = null)
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                }

                // Menu
                Menu(
                    navController = navController,
                    isMenuOpen = isMenuOpen,
                    onToggleMenu = { isMenuOpen = !isMenuOpen },
                    currentPage = "reservations",
                    userId = userId,
                    userViewModel = userViewModel
                )
            }
        }
    )

    // Pop-up de confirmation
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirmer l'annulation") },
            text = { Text("Êtes-vous sûr de vouloir annuler cette réservation ?") },
            confirmButton = {
                TextButton(onClick = {
                    selectedReservationId?.let {
                        isProcessingDelete = true
                        reservationViewModel.deleteReservation(it, userId)
                    }
                    showDialog = false
                }) {
                    Text("Oui", color = darkModeColorGreen, fontSize = 18.sp)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Non", color = Color.Gray, fontSize = 18.sp)
                }
            }
        )
    }

    // Alerte de succès ou d’échec
    if (showAlert) {
        Alert(
            isSuccess = isAlertSuccess,
            message = alertMessage,
            onDismiss = {
                showAlert = false
                reservationViewModel.fetchReservations(userId)
            }
        )
    }
}
