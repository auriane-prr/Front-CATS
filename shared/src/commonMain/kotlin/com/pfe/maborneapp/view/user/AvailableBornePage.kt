package com.pfe.maborneapp.view.user

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.pfe.maborneapp.utils.DarkModeGreen
import com.pfe.maborneapp.viewmodel.factories.user.ReservationViewModelFactory
import com.pfe.maborneapp.viewmodel.user.ReservationViewModel
import com.pfe.maborneapp.models.Borne
import com.pfe.maborneapp.models.Reservation
import com.pfe.maborneapp.models.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvailableBornesPage(
    navController: NavHostController,
    startDate: String,
    endDate: String,
    userId: String
) {
    val reservationViewModel: ReservationViewModel = viewModel(factory = ReservationViewModelFactory())
    val availableBornes by reservationViewModel.availableBornes.collectAsState()
    var selectedBorne by remember { mutableStateOf<Borne?>(null) }

    val darkModeColorGreen = if (isSystemInDarkTheme()) DarkModeGreen else Color(0xFF045C3C)

    // Charger les bornes disponibles
    LaunchedEffect(startDate, endDate) {
        reservationViewModel.fetchAvailableBornes(startDate, endDate)
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Bornes disponibles") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                }
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                ) {
                    Text("Annuler")
                }

                Button(
                    onClick = {
                        selectedBorne?.let { borne ->
                            val reservation = Reservation(
                                id = "", // Généré par le backend
                                borne = borne,
                                user = User(userId, "user@example.com", "User"), // Utilisateur actuel
                                dateDebut = startDate,
                                dateFin = endDate
                            )
                            reservationViewModel.createReservation(reservation)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = darkModeColorGreen),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Confirmer", color = Color.White)
                }
            }
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                if (availableBornes == null) {
                    // Afficher un loader pendant le chargement
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = darkModeColorGreen)
                    }
                } else if (availableBornes!!.isEmpty()) {
                    // Aucune borne disponible
                    Text(
                        text = "Aucune borne disponible.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                } else {
                    // Afficher les bornes disponibles
                    availableBornes!!.forEach { borne ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedBorne = borne }
                                .padding(vertical = 8.dp)
                                .background(
                                    color = if (selectedBorne == borne) darkModeColorGreen else Color(0xFFE0E0E0),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Borne ${borne.numero} - ${borne.status}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (selectedBorne == borne) Color.White else Color.Black
                            )
                        }
                    }
                }
            }
        }
    )

    // Gestion de la création de réservation
    val creationStatus by reservationViewModel.creationStatus.collectAsState()
    LaunchedEffect(creationStatus) {
        if (creationStatus == true) {
            // Retourner à la page des réservations après succès
            navController.popBackStack("reservations/$userId", inclusive = false)
        }
    }
}
