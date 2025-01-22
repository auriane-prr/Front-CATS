package com.pfe.maborneapp.view.user

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pfe.maborneapp.utils.*
import com.pfe.maborneapp.viewmodel.user.ReservationViewModel
import com.pfe.maborneapp.models.Borne
import com.pfe.maborneapp.models.CarteId
import com.pfe.maborneapp.models.Reservation
import com.pfe.maborneapp.models.User
import com.pfe.maborneapp.view.components.Alert
import com.pfe.maborneapp.viewmodel.CarteViewModel
import com.pfe.maborneapp.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvailableBornesPage(
    navController: NavController,
    reservationViewModel: ReservationViewModel,
    carteViewModel: CarteViewModel,
    userViewModel: UserViewModel,
) {

    val userId by userViewModel.userId.collectAsState()
    val availableBornes by reservationViewModel.availableBornes.collectAsState()
    var selectedBorne by remember { mutableStateOf<Borne?>(null) }
    var selectedCarteName by remember { mutableStateOf<String?>(null) }

    val startDate by reservationViewModel.startTime.collectAsState()
    val endDate by reservationViewModel.endTime.collectAsState()
    val selectedCarte by reservationViewModel.selectedCarte.collectAsState()

    var showAlert by remember { mutableStateOf(false) }
    var alertMessage by remember { mutableStateOf("") }
    var isAlertSuccess by remember { mutableStateOf(false) }

    val darkModeColorGreen = if (isSystemInDarkTheme()) DarkModeGreen else Color(0xFF045C3C)

    LaunchedEffect(startDate, endDate, selectedCarte) {
        selectedCarte?.let { carte ->
            reservationViewModel.fetchAvailableBornesByCarte(startDate, endDate, CarteId(carte.id))
        }
    }

    LaunchedEffect(selectedCarte) {
        selectedCarteName = selectedCarte?.nom
        println("DEBUG: Carte sélectionnée : ${selectedCarte?.nom}")
    }


    // Formater les dates
    fun formatDateRange(start: String, end: String): String {
        val startParts = start.split("T")
        val date = startParts[0].split("-")
        val startTime = startParts[1].split(":")

        val endParts = end.split("T")
        val endTime = endParts[1].split(":")

        return "Le ${date[2]}/${date[1]}/${date[0]} de ${startTime[0]}:${startTime[1]} à ${endTime[0]}:${endTime[1]}"
    }

    val formattedDateRange = formatDateRange(startDate, endDate)

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Bornes disponibles") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigateAndClear("newReservation", reservationViewModel)
                    }) {
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
                    onClick = { navController.navigateAndClear("reservations", reservationViewModel) },
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                ) {
                    Text("Annuler", color = Color.Gray)
                }

                Button(
                    onClick = {
                        if (selectedBorne == null) {
                            isAlertSuccess = false
                            alertMessage = "Veuillez sélectionner une borne à réserver."
                            showAlert = true
                        } else {
                            selectedBorne?.let { borne ->
                                val reservation = Reservation(
                                    id = "",
                                    borne = borne,
                                    user = User(userId, "user@example.com", "User"),
                                    dateDebut = startDate,
                                    dateFin = endDate
                                )
                                reservationViewModel.createReservation(reservation)
                            }
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
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
                    .fillMaxSize()
                    .verticalScroll(scrollState),
            ) {
                Text(
                    text = formattedDateRange,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )
                Text(
                    text = selectedCarteName ?: "Nom de la carte non disponible",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(16.dp))
                Divider(color = darkModeColorGreen, thickness = (1.5).dp)
                Spacer(modifier = Modifier.height(16.dp))

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
                                    color = if (selectedBorne == borne) Color(0xFFBDD3D0) else Color(
                                        0xFFE0E0E0
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Borne ${borne.numero} - ${borne.status}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Black
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
        creationStatus?.let {
            println("DEBUG: Création status = $it")
            if (it) {
                isAlertSuccess = true
                alertMessage = "Votre réservation a bien été prise en compte."
                reservationViewModel.clearReservationDetailsAndState() // Réinitialise tous les états
                navController.navigateAndClear("reservations", reservationViewModel)
            } else {
                isAlertSuccess = false
                alertMessage = reservationViewModel.lastErrorMessage
                    ?: "Erreur lors de la création de la réservation. Veuillez réessayer."
            }
            showAlert = true
            reservationViewModel.resetCreationStatus() // Réinitialise le statut de création
        }
    }

    // Afficher l'alerte d'erreur ou de succès
    if (showAlert) {
        Alert(
            isSuccess = isAlertSuccess,
            message = alertMessage,
            onDismiss = {
                showAlert = false
                if (isAlertSuccess) {
                    navController.navigateAndClear("reservations", reservationViewModel)
                }
            }
        )
    }
}