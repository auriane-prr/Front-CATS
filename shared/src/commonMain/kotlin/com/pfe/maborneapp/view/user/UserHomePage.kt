package com.pfe.maborneapp.view.user

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pfe.maborneapp.models.Carte
import com.pfe.maborneapp.view.user.components.NetworkImage
import com.pfe.maborneapp.viewmodel.user.CarteViewModel
import com.pfe.maborneapp.viewmodel.factories.user.CarteViewModelFactory
import com.pfe.maborneapp.view.user.components.Menu

@Composable
fun UserHomePage(navController: NavHostController, userEmail: String) {
    // Initialisation du ViewModel avec la factory
    val carteViewModel: CarteViewModel = viewModel(factory = CarteViewModelFactory())
    val cartes by carteViewModel.cartes.collectAsState()
    val selectedCarteImageUrl by carteViewModel.selectedCarteImageUrl.collectAsState()

    val greenColor = Color(0xFF045C3C)
    var isMenuOpen by remember { mutableStateOf(false) }
    var selectedCarteId by remember { mutableStateOf("") }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        content = { padding ->
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                ) {
                    // Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Bonjour $userEmail",
                            style = MaterialTheme.typography.titleLarge,
                            color = greenColor,
                                    modifier = Modifier
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Dropdown Menu pour sélectionner une carte
                    CarteDropdownMenu(
                        cartes = cartes,
                        selectedCarteId = selectedCarteId,
                        onCarteSelected = { carteId ->
                            selectedCarteId = carteId
                            carteViewModel.selectCarte(carteId) // Met à jour l'image sélectionnée
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Affichage de l'image de la carte sélectionnée
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp)
                            .background(Color.LightGray, RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (selectedCarteImageUrl != null) {
                            NetworkImage(
                                imageUrl = selectedCarteImageUrl,
                                contentDescription = "Carte Image",
                                modifier = Modifier
                                    .width(300.dp) // Taille fixe pour tester
                                    .height(200.dp)
                            )

                        } else {
                            Text(
                                text = "Sélectionnez une carte",
                                color = Color.Gray
                            )
                        }
                    }


                    Spacer(modifier = Modifier.height(16.dp))

                    // Rectangle pour les légendes
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .background(Color.LightGray, RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Légendes à venir",
                            color = Color.Gray
                        )
                    }
                }

                // Menu avec gestion de l'icône
                Menu(
                    navController = navController,
                    isMenuOpen = isMenuOpen,
                    onToggleMenu = { isMenuOpen = !isMenuOpen },
                    currentPage = "home",
                    userEmail = userEmail
                )
            }
        }
    )
}

@Composable
fun CarteDropdownMenu(
    cartes: List<Carte>, // Liste des cartes récupérées
    selectedCarteId: String, // ID de la carte sélectionnée
    onCarteSelected: (String) -> Unit // Callback pour gérer la sélection d'une carte
) {
    var expanded by remember { mutableStateOf(false) }
    val greenColor = Color(0xFF045C3C)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = greenColor
            ),
            border = BorderStroke(1.dp, greenColor)
        ) {
            val selectedCarteName = cartes.find { it.id == selectedCarteId }?.nom ?: "Sélectionnez une carte"
            Text(text = selectedCarteName, color = greenColor)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            cartes.forEach { carte ->
                DropdownMenuItem(
                    text = { Text(carte.nom) },
                    onClick = {
                        onCarteSelected(carte.id)
                        expanded = false
                    }
                )
            }
        }
    }
}
