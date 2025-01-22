package com.pfe.maborneapp.view.user

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pfe.maborneapp.models.CarteId
import com.pfe.maborneapp.utils.*
import com.pfe.maborneapp.view.components.Alert
import com.pfe.maborneapp.view.user.components.TimePickerDialog
import com.pfe.maborneapp.viewmodel.CarteViewModel
import com.pfe.maborneapp.viewmodel.UserViewModel
import com.pfe.maborneapp.viewmodel.user.ReservationViewModel
import kotlinx.datetime.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewReservationPage(
    navController: NavController,
    reservationViewModel: ReservationViewModel,
    carteViewModel: CarteViewModel
) {

    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }
    val creationStatus by reservationViewModel.creationStatus.collectAsState()

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    val darkModeColorGreen = if (isSystemInDarkTheme()) DarkModeGreen else Color(0xFF045C3C)

    var showAlert by remember { mutableStateOf(false) }
    var alertMessage by remember { mutableStateOf("") }
    var isAlertSuccess by remember { mutableStateOf(false) }

    val selectedDate by reservationViewModel.selectedDate.collectAsState()
    val startTime by reservationViewModel.startTime.collectAsState()
    val endTime by reservationViewModel.endTime.collectAsState()

    val cartes by carteViewModel.carte.collectAsState()
    var showCarteDropdown by remember { mutableStateOf(false) }
    val selectedCarte = reservationViewModel.selectedCarte

    LaunchedEffect(Unit) {
        carteViewModel.fetchCartes()
    }

    // Fonction de validation des champs
    fun validateInputs() {
        if (reservationViewModel.selectedDate.value.isEmpty() || reservationViewModel.startTime.value.isEmpty() || reservationViewModel.endTime.value.isEmpty() || selectedCarte.value == null) {
            alertMessage = "Vous devez remplir tous les champs"
            isAlertSuccess = false
            showAlert = true
            return
        }

        // Vérification des heures
        val startParts = reservationViewModel.startTime.value.split(":")
        val endParts = reservationViewModel.endTime.value.split(":")

        val startMinutes = startParts[0].toInt() * 60 + startParts[1].toInt()
        val endMinutes = endParts[0].toInt() * 60 + endParts[1].toInt()

        if (endMinutes <= startMinutes) {
            alertMessage = "Veuillez entrer des horaires corrects"
            isAlertSuccess = false
            return
        }

        // Appel de la méthode `setupReservationDetails` pour configurer les détails
        val start = "${reservationViewModel.selectedDate.value.split("-").reversed().joinToString("-")}T${reservationViewModel.startTime.value}:00"
        val end = "${reservationViewModel.selectedDate.value.split("-").reversed().joinToString("-")}T${reservationViewModel.endTime.value}:00"

        selectedCarte.value?.let {
            reservationViewModel.setupReservationDetails(reservationViewModel.selectedDate.value, start, end, it)
            reservationViewModel.fetchAvailableBornesByCarte(start, end, CarteId(it.id))
        }

        // Navigation vers la page "availableBornes"
        navController.navigate("availableBornes")
    }

    // Fonction de validation des dates
    fun validateDateAndCloseDialog() {
        val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date // Date actuelle sans l'heure

        try {

            val dateParts = reservationViewModel.selectedDate.value.split("-")
            val formattedDate = "${dateParts[2]}-${dateParts[1]}-${dateParts[0]}"
            val selectedLocalDate = LocalDate.parse(formattedDate)

            if (selectedLocalDate < currentDate) {
                alertMessage = "Veuillez entrer une date correcte"
                isAlertSuccess = false
                showAlert = true
            } else {
                showDatePicker = false
            }
        } catch (e: Exception) {
            alertMessage = "Veuillez entrer une date correcte"
            isAlertSuccess = false
            showAlert = true
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Nouvelle réservation") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigateAndClear("reservations", reservationViewModel)
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Sélecteur de carte
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.TopStart)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                showCarteDropdown = !showCarteDropdown
                            }
                            .background(Color(0xFFBDD3D0), shape = MaterialTheme.shapes.small)
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = selectedCarte.value?.nom ?: "Sélectionnez une carte",
                            modifier = Modifier.weight(1f),
                            color = Color.Black,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Icon(
                            imageVector = Icons.Default.Map,
                            contentDescription = "Sélecteur de carte"
                        )
                    }

                    DropdownMenu(
                        expanded = showCarteDropdown,
                        onDismissRequest = { showCarteDropdown = false }
                    ) {
                        cartes.forEach { carte ->
                            DropdownMenuItem(
                                text = { Text(carte.nom) },
                                onClick = {
                                    selectedCarte.value = carte // Mettre à jour la carte sélectionnée
                                    showCarteDropdown = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Sélecteur de date
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showDatePicker = true
                        }
                        .background(Color(0xFFBDD3D0), shape = MaterialTheme.shapes.small)
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = selectedDate.ifEmpty { "Sélectionnez une date" },
                        modifier = Modifier.weight(1f),
                        color = Color.Black,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = "Sélecteur de date"
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Sélecteur d'heure début
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showStartTimePicker = true
                        }
                        .background(Color(0xFFBDD3D0), shape = MaterialTheme.shapes.small)
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = startTime.ifEmpty { "Sélectionnez une heure de début" },
                        modifier = Modifier.weight(1f),
                        color = Color.Black,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = "Heure de début"
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Sélecteur d'heure fin
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showEndTimePicker = true
                        }
                        .background(Color(0xFFBDD3D0), shape = MaterialTheme.shapes.small)
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = endTime.ifEmpty { "Sélectionnez une heure de fin" },
                        modifier = Modifier.weight(1f),
                        color = Color.Black,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = "Heure de fin"
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Bouton pour récupérer les bornes disponibles
                Button(
                    onClick = { validateInputs() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = darkModeColorGreen)
                ) {
                    Text(
                        text = "Voir les bornes disponibles",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }

            // Afficher l'alerte
            if (showAlert) {
                Alert(
                    isSuccess = isAlertSuccess,
                    message = alertMessage,
                    onDismiss = { showAlert = false }
                )
            }

            // DatePickerDialog
            if (showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                val millis = datePickerState.selectedDateMillis
                                if (millis != null) {
                                    val localDate = Instant.fromEpochMilliseconds(millis)
                                        .toLocalDateTime(TimeZone.currentSystemDefault()).date
                                    val formattedDate = listOf(
                                        localDate.dayOfMonth.toString().padStart(2, '0'),
                                        localDate.monthNumber.toString().padStart(2, '0'),
                                        localDate.year.toString()
                                    ).joinToString("-")
                                    reservationViewModel.selectedDate.value = formattedDate
                                    println("DEBUG, Date sélectionnée : ${reservationViewModel.selectedDate.value}")
                                }
                                validateDateAndCloseDialog()
                            }
                        ) {
                            Text("OK", color = darkModeColorGreen)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDatePicker = false }) {
                            Text("Annuler", color = darkModeColorGreen)
                        }
                    }
                ) {
                    DatePicker(
                        state = datePickerState,
                        modifier = Modifier.fillMaxWidth(),
                        title = null,
                        headline = null,
                        showModeToggle = false,
                        colors = DatePickerDefaults.colors(
                            selectedDayContentColor = Color.White,
                            selectedDayContainerColor = darkModeColorGreen,
                            todayDateBorderColor = darkModeColorGreen,
                        )
                    )
                }
            }

            val timePickerState = rememberTimePickerState(is24Hour = true)

            // TimePickerDialog pour l'heure de début
            if (showStartTimePicker) {
                TimePickerDialog(
                    title = "Entrez une heure de début",
                    state = timePickerState,
                    darkModeColorGreen = darkModeColorGreen,
                    onCancel = { showStartTimePicker = false },
                    onConfirm = { hour, minute ->
                        reservationViewModel.startTime.value = "${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}"
                        println("DEBUG, Heure de début sélectionnée : ${reservationViewModel.startTime.value}")
                        showStartTimePicker = false
                    }
                )
            }

            // TimePickerDialog pour l'heure de fin
            if (showEndTimePicker) {
                TimePickerDialog(
                    title = "Entrez une heure de fin",
                    state = timePickerState,
                    darkModeColorGreen = darkModeColorGreen,
                    onCancel = { showEndTimePicker = false },
                    onConfirm = { hour, minute ->
                        reservationViewModel.endTime.value = "${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}"
                        println("DEBUG, Heure de fin sélectionnée : ${reservationViewModel.endTime.value}")
                        showEndTimePicker = false
                    }
                )
            }
        }
    )

    LaunchedEffect(creationStatus) {
        if (creationStatus == true) {
            println("DEBUG, Création de réservation réussie, retour à la page précédente")
            navController.goBack()
        }
    }
}
