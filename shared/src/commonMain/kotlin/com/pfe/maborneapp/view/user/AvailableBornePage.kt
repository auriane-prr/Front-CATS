package com.pfe.maborneapp.view.user

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.pfe.maborneapp.utils.DarkModeGreen
import com.pfe.maborneapp.viewmodel.factories.user.ReservationViewModelFactory
import com.pfe.maborneapp.viewmodel.user.ReservationViewModel
import com.pfe.maborneapp.models.Borne
import com.pfe.maborneapp.models.CarteId
import com.pfe.maborneapp.models.Reservation
import com.pfe.maborneapp.models.User
import com.pfe.maborneapp.view.components.Alert
import com.pfe.maborneapp.viewmodel.CarteViewModel
import com.pfe.maborneapp.viewmodel.factories.CarteViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvailableBornesPage(
    navController: NavHostController,
    startDate: String,
    endDate: String,
    userId: String,
    carteId: CarteId,
) {
    val reservationViewModel: ReservationViewModel = viewModel(factory = ReservationViewModelFactory())
    val carteViewModel: CarteViewModel = viewModel(factory = CarteViewModelFactory())
    val availableBornes by reservationViewModel.availableBornes.collectAsState()
    var selectedBorne by remember { mutableStateOf<Borne?>(null) }
    var selectedCarteName by remember { mutableStateOf<String?>(null) }
    val selectedCarte by carteViewModel.selectedCarte.collectAsState()

    var showAlert by remember { mutableStateOf(false) }
    var alertMessage by remember { mutableStateOf("") }
    var isAlertSuccess by remember { mutableStateOf(false) }

    val darkModeColorGreen = if (isSystemInDarkTheme()) DarkModeGreen else Color(0xFF045C3C)

    // Charger les bornes disponibles
    LaunchedEffect(startDate, endDate) {
        reservationViewModel.fetchAvailableBornesByCarte(startDate, endDate, carteId)
    }

    LaunchedEffect(carteId) {
        try {
            val carte = carteViewModel.fetchCarteById(carteId._id)
            if (carte != null) {
                selectedCarteName = carte.nom
                println("DEBUG: Carte trouvée : ${carte.nom}")
            } else {
                println("DEBUG: Aucune carte trouvée pour l'ID ${carteId._id}")
            }
        } catch (e: Exception) {
            println("Erreur lors de la récupération de la carte : ${e.message}")
        }
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
                    Text("Annuler", color = Color.Gray)
                }

                Button(
                    onClick = {
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
                                    color = if (selectedBorne == borne) Color(0xFFBDD3D0) else Color(0xFFE0E0E0),
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
            if (it) {
                isAlertSuccess = true
                alertMessage = "Votre réservation a bien été prise en compte"
                showAlert = true
            } else {
                isAlertSuccess = false
                alertMessage = "Erreur lors de la création de la réservation. Veuillez réessayer."
                showAlert = true
            }
        }
    }

    // Afficher l'alerte d'erreur ou de succès
    if (showAlert) {
        Alert(
            isSuccess = isAlertSuccess,
            message = alertMessage,
            onDismiss = {
                showAlert = false
                navController.popBackStack("reservations/$userId", inclusive = false)
            }
        )
    }
}