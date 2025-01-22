package com.pfe.maborneapp.view.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pfe.maborneapp.models.CarteId
import com.pfe.maborneapp.utils.*
import com.pfe.maborneapp.view.admin.components.AdminMenu
import com.pfe.maborneapp.view.components.CarteDropdownMenu
import com.pfe.maborneapp.view.admin.components.SignalementList
import com.pfe.maborneapp.viewmodel.*

@Composable
fun AdminSignalementPage(
    navController: NavController,
    carteViewModel: CarteViewModel,
    signalementViewModel: SignalementViewModel,
    userViewModel: UserViewModel
) {
    val darkModeColorGreen = if (isSystemInDarkTheme()) DarkModeGreen else Color(0xFF045C3C)

    val cartes by carteViewModel.carte.collectAsState()
    val selectedCarte by carteViewModel.selectedCarte.collectAsState()
    val signalements by signalementViewModel.signalements.collectAsState()
    val isLoadingCartes by carteViewModel.isLoading.collectAsState()
    val isLoadingSignalements by signalementViewModel.isLoading.collectAsState()
    val errorLoadingCartes by carteViewModel.errorMessage.collectAsState()

    var isMenuOpen by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Charger les cartes au montage
    LaunchedEffect(Unit) {
        carteViewModel.fetchCartes()
    }

    // Sélectionner la carte par défaut si aucune n'est sélectionnée
    LaunchedEffect(cartes) {
        if (cartes.isNotEmpty() && selectedCarte == null) {
            carteViewModel.setSelectedCarte(cartes.find { it.nom == "CATS de Montpellier" })
        }
    }

    val selectedCarteId by carteViewModel.selectedCarteId.collectAsState()

    LaunchedEffect(selectedCarteId) {
        selectedCarteId?.let { carteId ->
            println("DEBUG: Chargement des signalements pour la carte sélectionnée - ID: $carteId")
            signalementViewModel.fetchSignalementsByCarte(CarteId(carteId))
        } ?: println("DEBUG: Aucune carte sélectionnée")
    }


    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {

                Text(
                    text = "Signalements des bornes",
                    style = MaterialTheme.typography.titleLarge,
                    color = darkModeColorGreen,
                    modifier = Modifier.padding(vertical = 10.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                // Menu déroulant pour sélectionner une carte
                if (isLoadingCartes) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        color = darkModeColorGreen)
                } else if (cartes.isNotEmpty()) {
                    CarteDropdownMenu(
                        cartes = cartes,
                        selectedCarte = selectedCarte,
                        onCarteSelected = {
                            carteViewModel.setSelectedCarte(it)
                            signalementViewModel.fetchSignalementsByCarte(CarteId(it.id)) // Recharger les signalements
                        }
                    )
                } else {
                    Text(
                        text = errorLoadingCartes ?: "Erreur lors du chargement des cartes.",
                        color = Color.Red,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Affichage des signalements ou du loader
                if (isLoadingSignalements) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        color = darkModeColorGreen
                    )
                } else if (signalements.isNullOrEmpty()) {
                    Text(
                        text = "Aucun signalement trouvé pour cette carte.",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                } else {
                    SignalementList(
                        signalements = signalements!!,
                        onUpdateStatus = { borneId, newStatus, signalementId ->
                            signalementViewModel.updateBorneStatus(
                                borneId,
                                newStatus,
                                onSuccess = {
                                    signalementId?.let {
                                        signalementViewModel.closeSignalement(
                                            it,
                                            onSuccess = {
                                                selectedCarte?.let { carte ->
                                                    signalementViewModel.fetchSignalementsByCarte(
                                                        CarteId(carte.id)
                                                    )
                                                }
                                            },
                                            onError = { error ->
                                                errorMessage = error
                                            }
                                        )
                                    }
                                },
                                onError = { error ->
                                    errorMessage = error
                                }
                            )
                        }
                    )

                }
            }

            // Menu Admin
            AdminMenu(
                navController = navController,
                isMenuOpen = isMenuOpen,
                onToggleMenu = { isMenuOpen = !isMenuOpen },
                currentPage = "adminSignalement",
                carteViewModel = carteViewModel,
                userViewModel = userViewModel
            )
        }
    )
}
