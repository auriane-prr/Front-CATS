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
import com.pfe.maborneapp.models.Borne
import com.pfe.maborneapp.models.Reservation
import com.pfe.maborneapp.models.User
import com.pfe.maborneapp.utils.DarkModeGreen
import com.pfe.maborneapp.viewmodel.factories.user.ReservationViewModelFactory
import com.pfe.maborneapp.viewmodel.user.ReservationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewReservationPage(navController: NavHostController, userId: String) {
    val reservationViewModel: ReservationViewModel = viewModel(factory = ReservationViewModelFactory())

    var selectedDate by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }
    val availableBornes by reservationViewModel.availableBornes.collectAsState()
    var selectedBorne by remember { mutableStateOf<Borne?>(null) }
    val creationStatus by reservationViewModel.creationStatus.collectAsState()

    val darkModeColorGreen = if (isSystemInDarkTheme()) DarkModeGreen else Color(0xFF045C3C)

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Nouvelle réservation") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
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
                OutlinedTextField(
                    value = selectedDate,
                    onValueChange = { selectedDate = it },
                    label = { Text("Sélectionnez une date (JJ-MM-AAAA)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Sélecteur d'heure début
                OutlinedTextField(
                    value = startTime,
                    onValueChange = { startTime = it },
                    label = { Text("Heure de début (HH:mm)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Sélecteur d'heure fin
                OutlinedTextField(
                    value = endTime,
                    onValueChange = { endTime = it },
                    label = { Text("Heure de fin (HH:mm)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Bouton pour récupérer les bornes disponibles
                Button(
                    onClick = {
                        val start = "${selectedDate.split("-").reversed().joinToString("-")}T${startTime}:00"
                        val end = "${selectedDate.split("-").reversed().joinToString("-")}T${endTime}:00"

                        // Naviguer vers la page d'affichage des bornes disponibles
                        val route = "availableBornes/$start/$end/$userId"
                        navController.navigate(route)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = darkModeColorGreen)
                ) {
                    Text(text = "Afficher les bornes disponibles", color = Color.White)
                }
            }
        }
    )

    // Gestion de la création de réservation
    LaunchedEffect(creationStatus) {
        if (creationStatus == true) {
            navController.popBackStack() // Retourner à la page précédente
        }
    }
}