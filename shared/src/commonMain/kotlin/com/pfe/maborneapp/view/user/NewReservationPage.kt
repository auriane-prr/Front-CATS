package com.pfe.maborneapp.view.user

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
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
import com.pfe.maborneapp.view.components.Alert
import com.pfe.maborneapp.view.user.components.TimePickerDialog
import com.pfe.maborneapp.viewmodel.factories.user.ReservationViewModelFactory
import com.pfe.maborneapp.viewmodel.user.ReservationViewModel
import kotlinx.datetime.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewReservationPage(navController: NavHostController, userId: String) {
    val reservationViewModel: ReservationViewModel = viewModel(factory = ReservationViewModelFactory())

    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }
    val creationStatus by reservationViewModel.creationStatus.collectAsState()

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    val darkModeColorGreen = if (isSystemInDarkTheme()) DarkModeGreen else Color(0xFF045C3C)

    var showAlert by remember { mutableStateOf(false) }
    var alertMessage by remember { mutableStateOf("") }
    var isAlertSuccess by remember { mutableStateOf(false) }

    val selectedDate = reservationViewModel.selectedDate.value
    val startTime = reservationViewModel.startTime.value
    val endTime = reservationViewModel.endTime.value

    // Fonction de validation des champs
    fun validateInputs() {
        // Vérifier si tous les champs sont remplis
        if (reservationViewModel.selectedDate.value.isEmpty() || reservationViewModel.startTime.value.isEmpty() || reservationViewModel.endTime.value.isEmpty()) {
            alertMessage = "Vous devez remplir tous les champs"
            isAlertSuccess = false
            showAlert = true
            return
        }

        // Vérification de l'heure de fin par rapport à l'heure de début
        val startParts = reservationViewModel.startTime.value.split(":")
        val endParts = reservationViewModel.endTime.value.split(":")

        val startMinutes = startParts[0].toInt() * 60 + startParts[1].toInt() // Convertir en minutes
        val endMinutes = endParts[0].toInt() * 60 + endParts[1].toInt() // Convertir en minutes

        // Vérifier que l'heure de fin est après l'heure de début
        if (endMinutes <= startMinutes) {
            alertMessage = "Veuillez entrer des horaires corrects"
            isAlertSuccess = false
            showAlert = true
            return
        }

        // Si tout est valide, procéder à l'action
        val start = "${
            reservationViewModel.selectedDate.value.split("-").reversed().joinToString("-")
        }T${reservationViewModel.startTime.value}:00"
        val end =
            "${reservationViewModel.selectedDate.value.split("-").reversed().joinToString("-")}T${reservationViewModel.endTime.value}:00"
        println("DEBUG, Bouton cliqué avec start: $start, end: $end")
        val route = "availableBornes/$start/$end/$userId"
        navController.navigate(route)
    }

    // Fonction de validation des dates
    fun validateDateAndCloseDialog() {
        // Récupérer la date actuelle sans l'heure
        val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date // Date actuelle sans l'heure

        try {
            // Convertir la date sélectionnée de 'jj-MM-AAAA' à 'yyyy-MM-dd'
            val dateParts = reservationViewModel.selectedDate.value.split("-")
            val formattedDate = "${dateParts[2]}-${dateParts[1]}-${dateParts[0]}" // Format yyyy-MM-dd

            // Convertir la date formatée en LocalDate
            val selectedLocalDate = LocalDate.parse(formattedDate)

            // Vérifier si la date sélectionnée est antérieure à la date actuelle
            if (selectedLocalDate <= currentDate) {
                alertMessage = "Veuillez entrer une date correcte"
                isAlertSuccess = false
                showAlert = true
            } else {
                // Si la date est correcte, fermer le DatePicker
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
                        navController.popBackStack()
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
                        text = if (selectedDate.isEmpty()) "Sélectionnez une date" else selectedDate,
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
                        text = if (startTime.isEmpty()) "Sélectionnez une heure de début" else startTime,
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
                        text = if (endTime.isEmpty()) "Sélectionnez une heure de fin" else endTime,
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

                                    // Mettre à jour la date dans le ViewModel
                                    val formattedDate = listOf(
                                        localDate.dayOfMonth.toString().padStart(2, '0'),
                                        localDate.monthNumber.toString().padStart(2, '0'),
                                        localDate.year.toString()
                                    ).joinToString("-")

                                    // Mettre à jour le ViewModel avec la nouvelle date
                                    reservationViewModel.selectedDate.value = formattedDate

                                    println("DEBUG, Date sélectionnée : ${reservationViewModel.selectedDate.value}")
                                }

                                // Valider la date après la mise à jour
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
            navController.popBackStack()
        }
    }
}
