package com.pfe.maborneapp.view.user

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.pfe.maborneapp.utils.DarkModeGreen
import com.pfe.maborneapp.view.user.components.*
import com.pfe.maborneapp.viewmodel.factories.user.ReservationViewModelFactory
import com.pfe.maborneapp.viewmodel.factories.user.UserViewModelFactory
import com.pfe.maborneapp.viewmodel.user.ReservationViewModel
import com.pfe.maborneapp.viewmodel.user.UserViewModel

@Composable
fun ReservationPage(navController: NavHostController, userId: String) {
    val userViewModel: UserViewModel = viewModel(factory = UserViewModelFactory())
    val reservationViewModel: ReservationViewModel = viewModel(factory = ReservationViewModelFactory())
    val reservations by reservationViewModel.reservations.collectAsState()

    LaunchedEffect(userId) {
        println("DEBUG: Lancement de fetchReservations pour userId = $userId")
        userViewModel.fetchUserEmail(userId)
        reservationViewModel.fetchReservations(userId)
    }

    var isMenuOpen by remember { mutableStateOf(false) }
    var showReservationModal by remember { mutableStateOf(false) } // État de la modale
    val darkModeColorGreen = if (isSystemInDarkTheme()) DarkModeGreen else Color(0xFF045C3C)

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        content = {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxSize()
                ) {
                    // Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Mes réservations",
                            style = MaterialTheme.typography.titleLarge,
                            color = darkModeColorGreen,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Liste des réservations
                    reservations?.let { resList ->
                        if (resList.isEmpty()) {
                            Text(text = "Aucune réservation trouvée.")
                        } else {
                            resList.forEach { reservation ->
                                ReservationCard(reservation = reservation)
                            }
                        }
                    } ?: Text(text = "Chargement des réservations...")

                    Spacer(modifier = Modifier.height(16.dp))

                    // Bouton nouvelle réservation
                    Button(
                        onClick = {
                            println("DEBUG: Bouton 'Nouvelle réservation' cliqué")
                            showReservationModal = true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = darkModeColorGreen)
                    ) {
                        Text(text = "Nouvelle réservation", color = Color.White)
                    }
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

            // Affichage de la modale si l'état est activé
            if (showReservationModal) {
                ReservationModal(
                    reservationViewModel = reservationViewModel,
                    userId = userId,
                    onClose = { showReservationModal = false }, // Ferme la modale
                    onReservationSuccess = {
                        // Recharge les réservations après succès
                        reservationViewModel.fetchReservations(userId)
                    }
                )
            }
        }
    )
}