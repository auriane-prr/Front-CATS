package com.pfe.maborneapp.view.user

import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.pfe.maborneapp.utils.DarkModeGreen
import com.pfe.maborneapp.view.user.components.Menu
import com.pfe.maborneapp.models.Reservation
import com.pfe.maborneapp.view.user.components.ReservationCard
import com.pfe.maborneapp.viewmodel.factories.user.ReservationViewModelFactory
import com.pfe.maborneapp.viewmodel.factories.user.UserViewModelFactory
import com.pfe.maborneapp.viewmodel.user.ReservationViewModel
import com.pfe.maborneapp.viewmodel.user.UserViewModel

@Composable
fun ReservationPage(navController: NavHostController, userId: String) {
    val userViewModel: UserViewModel = viewModel(factory = UserViewModelFactory())
    val reservationViewModel: ReservationViewModel = viewModel(factory = ReservationViewModelFactory())

    val reservations by reservationViewModel.reservations.collectAsState()
    val upcomingReservations = remember { mutableStateListOf<Reservation>() }
    val pastReservations = remember { mutableStateListOf<Reservation>() }

    val isLoading by reservationViewModel.isLoading.collectAsState()

    LaunchedEffect(userId) {
        userViewModel.fetchUserEmail(userId)
        reservationViewModel.fetchReservations(userId)
    }

    // Mise à jour des listes triées
    LaunchedEffect(reservations) {
        if (reservations != null) {
            upcomingReservations.clear()
            pastReservations.clear()
            val (upcoming, past) = reservationViewModel.getSortedReservations()
            upcomingReservations.addAll(upcoming)
            pastReservations.addAll(past)
        }
    }

    var isMenuOpen by remember { mutableStateOf(false) }
    val darkModeColorGreen = if (isSystemInDarkTheme()) DarkModeGreen else Color(0xFF045C3C)

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("newReservation/$userId")
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
                                ReservationCard(reservation = reservation)
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
                                ReservationCard(reservation = reservation)
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
                    userId = userId
                )
            }
        }
    )
}
