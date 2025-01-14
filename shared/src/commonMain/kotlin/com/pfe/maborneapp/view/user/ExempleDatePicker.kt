package com.pfe.maborneapp.view.user

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExempleDatePicker(navController: NavHostController, userId: String) {
    println("DEBUG, NewReservationPage, Composable chargé avec userId: $userId")

    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf("Aucune date sélectionnée") }
    val datePickerState = rememberDatePickerState()

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
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Date sélectionnée : $selectedDate",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Button(
                    onClick = {
                        println("DEBUG, NewReservationPage, Bouton DatePickerDialog cliqué")
                        showDatePicker = true
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ouvrir le sélecteur de date")
                }
            }

            if (showDatePicker) {
                println("DEBUG, NewReservationPage, Affichage du DatePickerDialog")
                DatePickerDialog(
                    onDismissRequest = {
                        println("DEBUG, DatePickerDialog annulé")
                        showDatePicker = false
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                val millis = datePickerState.selectedDateMillis
                                if (millis != null) {
                                    val localDate = Instant.fromEpochMilliseconds(millis)
                                        .toLocalDateTime(TimeZone.currentSystemDefault()).date
                                    selectedDate =
                                        "${localDate.dayOfMonth}/${localDate.monthNumber}/${localDate.year}"
                                    println("DEBUG, Date sélectionnée : $selectedDate")
                                } else {
                                    println("DEBUG, Aucune date sélectionnée")
                                }
                                showDatePicker = false
                            }
                        ) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            println("DEBUG, DatePickerDialog annulé via bouton")
                            showDatePicker = false
                        }) {
                            Text("Annuler")
                        }
                    }
                ) {
                    DatePicker(
                        state = datePickerState,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    )
}
