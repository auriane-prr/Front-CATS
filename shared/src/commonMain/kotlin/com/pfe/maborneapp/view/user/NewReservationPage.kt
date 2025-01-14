package com.pfe.maborneapp.view.user

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.outlined.Keyboard
import androidx.compose.material.icons.outlined.Schedule
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
import com.pfe.maborneapp.view.user.components.TimePickerDialog
import kotlinx.datetime.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewReservationPage(navController: NavHostController, userId: String) {
    println("DEBUG, NewReservationPage, Composable chargé avec userId: $userId")

    val reservationViewModel: ReservationViewModel =
        viewModel(factory = ReservationViewModelFactory())

    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }
    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }
    val startTimePickerState = rememberTimePickerState()
    val endTimePickerState = rememberTimePickerState()
    val creationStatus by reservationViewModel.creationStatus.collectAsState()

    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf("") }
    val datePickerState = rememberDatePickerState()
    var showingPicker by remember { mutableStateOf(true) }

    val darkModeColorGreen = if (isSystemInDarkTheme()) DarkModeGreen else Color(0xFF045C3C)

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Nouvelle réservation") },
                navigationIcon = {
                    IconButton(onClick = {
                        println("DEBUG, NewReservationPage, Retour actionné")
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
                // Sélecteur de date
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            println("DEBUG: Row cliquée pour ouvrir le DatePicker.")
                            showDatePicker = true
                        }
                        .background(Color.LightGray, shape = MaterialTheme.shapes.small)
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
                        contentDescription = "Sélecteur de date",
                        tint = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Sélecteur d'heure début
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            println("DEBUG: Row cliquée pour ouvrir le TimePicker de début.")
                            showStartTimePicker = true
                        }
                        .background(Color.LightGray, shape = MaterialTheme.shapes.small)
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (startTime.isEmpty()) "Sélectionnez une heure de début" else startTime,
                        modifier = Modifier.weight(1f),
                        color = Color.Black,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Sélecteur d'heure fin
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            println("DEBUG: Row cliquée pour ouvrir le TimePicker de fin.")
                            showEndTimePicker = true
                        }
                        .background(Color.LightGray, shape = MaterialTheme.shapes.small)
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (endTime.isEmpty()) "Sélectionnez une heure de fin" else endTime,
                        modifier = Modifier.weight(1f),
                        color = Color.Black,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Bouton pour récupérer les bornes disponibles
                Button(
                    onClick = {
                        val start = "${
                            selectedDate.split("-").reversed().joinToString("-")
                        }T${startTime}:00"
                        val end =
                            "${selectedDate.split("-").reversed().joinToString("-")}T${endTime}:00"
                        println("DEBUG, Bouton cliqué avec start: $start, end: $end")
                        val route = "availableBornes/$start/$end/$userId"
                        navController.navigate(route)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = darkModeColorGreen)
                ) {
                    Text(text = "Afficher les bornes disponibles", color = Color.White)
                }
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

                                    selectedDate = listOf(
                                        localDate.dayOfMonth.toString().padStart(2, '0'),
                                        localDate.monthNumber.toString().padStart(2, '0'),
                                        localDate.year.toString()
                                    ).joinToString("-")

                                    println("DEBUG, Date sélectionnée : $selectedDate")
                                }
                                showDatePicker = false
                            }
                        ) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDatePicker = false }) {
                            Text("Annuler")
                        }
                    }
                ) {
                    DatePicker(state = datePickerState, modifier = Modifier.fillMaxWidth())
                }
            }

            // TimePickerDialog pour l'heure de début
            if (showStartTimePicker) {
                TimePickerDialog(
                    title = "Sélectionnez une heure de début",
                    onCancel = { showStartTimePicker = false },
                    onConfirm = { hour, minute ->
                        startTime = "${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}"
                        println("DEBUG, Heure de début sélectionnée : $startTime")
                        showStartTimePicker = false
                    },
                    toggle = {
                        IconButton(onClick = { showingPicker = !showingPicker }) {
                            val icon = if (showingPicker) Icons.Outlined.Keyboard else Icons.Outlined.Schedule
                            Icon(icon, contentDescription = null)
                        }
                    },
                    state = startTimePickerState
                )
            }

            // TimePickerDialog pour l'heure de fin
            if (showEndTimePicker) {
                TimePickerDialog(
                    title = "Sélectionnez une heure de fin",
                    onCancel = { showEndTimePicker = false },
                    onConfirm = { hour, minute ->
                        endTime = "${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}"
                        println("DEBUG, Heure de fin sélectionnée : $endTime")
                        showEndTimePicker = false
                    },
                    toggle = {
                        IconButton(onClick = { showingPicker = !showingPicker }) {
                            val icon = if (showingPicker) Icons.Outlined.Keyboard else Icons.Outlined.Schedule
                            Icon(icon, contentDescription = null)
                        }
                    },
                    state = endTimePickerState
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