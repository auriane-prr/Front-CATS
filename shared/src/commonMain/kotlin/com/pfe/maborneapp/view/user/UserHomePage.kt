package com.pfe.maborneapp.view.user

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pfe.maborneapp.utils.*
import com.pfe.maborneapp.view.user.components.*
import com.pfe.maborneapp.viewmodel.BorneViewModel
import com.pfe.maborneapp.view.components.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import com.pfe.maborneapp.models.CarteId
import com.pfe.maborneapp.utils.NavController
import com.pfe.maborneapp.view.components.image.*
import com.pfe.maborneapp.viewmodel.*

@Composable
fun UserHomePage(
    navController: NavController,
    userViewModel: UserViewModel,
    borneViewModel: BorneViewModel,
    signalementViewModel: SignalementViewModel,
    carteViewModel: CarteViewModel
) {
    val darkModeColorTitle = if (isSystemInDarkTheme()) DarkModeGreen else Color(0xFF045C3C)

    val userId by userViewModel.userId.collectAsState()
    val userEmail by userViewModel.userEmail.collectAsState()
    val selectedCarteImageUrl by carteViewModel.selectedCarteImageUrl.collectAsState()
    val selectedCarteLastModified by carteViewModel.selectedCarteLastModified.collectAsState()
    val etatBornes by borneViewModel.etatBornes.collectAsState()
    val cartes by carteViewModel.carte.collectAsState()
    val selectedCarte by carteViewModel.selectedCarte.collectAsState()
    val isLoadingCartes by carteViewModel.isLoading.collectAsState()
    val errorLoadingCartes by carteViewModel.errorMessage.collectAsState()
    val isLoadingBornes by borneViewModel.isLoading.collectAsState()

    var isMenuOpen by remember { mutableStateOf(false) }
    var alertVisible by remember { mutableStateOf(false) }
    var alertMessage by remember { mutableStateOf("") }
    var alertIsSuccess by remember { mutableStateOf(true) }
    var showZoomableMap by remember { mutableStateOf(false) }

    val carteId = CarteId(selectedCarte?.id ?: "")

    val darkModeColorGreen = if (isSystemInDarkTheme()) DarkModeGreen else Color(0xFF045C3C)

    // Charger les cartes au montage
    LaunchedEffect(Unit) {
        carteViewModel.fetchCartes()
    }

    LaunchedEffect(cartes) {
        if (cartes.isNotEmpty() && selectedCarte == null) {
            val defaultCarte = cartes.find { it.nom == "CATS de Montpellier" }
            if (defaultCarte != null) {
                carteViewModel.setSelectedCarte(defaultCarte) // Sélectionne la carte par défaut
                borneViewModel.fetchBornesByEtatAndCarte(CarteId(defaultCarte.id)) // Charge les bornes associées
            } else {
                println("DEBUG: Aucune carte par défaut trouvée.")
            }
        }
    }

    // Charger les détails de la carte sélectionnée
    LaunchedEffect(selectedCarte) {
        selectedCarte?.let {
            println("DEBUG: Chargement des détails pour la carte sélectionnée - ID: ${it.id}")
            carteViewModel.fetchCarteDetails(it.id)
        }
    }

    LaunchedEffect(userId) {
        userViewModel.fetchUserEmail(userId)
    }

    if (showZoomableMap) {
        ZoomableImageView(
            imageUrl = selectedCarteImageUrl,
            lastModified = selectedCarteLastModified,
            contentDescription = "Detailed Map",
            onClose = { showZoomableMap = false }
        )
    } else {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            content = {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = if (userEmail.isNotEmpty()) "Bonjour $userEmail" else "Chargement...",
                        style = MaterialTheme.typography.titleLarge,
                        color = darkModeColorTitle
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Menu déroulant pour sélectionner une carte
                    if (isLoadingCartes) {
                        CircularProgressIndicator(
                            color = darkModeColorGreen,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    } else if (cartes.isNotEmpty()) {
                        CarteDropdownMenu(
                            cartes = cartes,
                            selectedCarte = selectedCarte,
                            onCarteSelected = {
                                carteViewModel.setSelectedCarte(it)
                                borneViewModel.fetchBornesByEtatAndCarte(CarteId(it.id))
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

                    // Affichage de la carte sélectionnée
                    selectedCarte?.let {

                        Spacer(modifier = Modifier.height(16.dp))

                        NetworkImage(
                            imageUrl = selectedCarteImageUrl,
                            //lastModified = selectedCarteLastModified,
                            contentDescription = "Carte Image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showZoomableMap = true }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(text = "Bornes :", fontSize = 16.sp)

                        if (isLoadingBornes) {
                            CircularProgressIndicator(
                                color = darkModeColorGreen,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        } else {
                            etatBornes?.let {
                                Spacer(modifier = Modifier.height(16.dp))
                                BorneList(
                                    etatBornes = it,
                                    userId = userId,
                                    signalementViewModel = signalementViewModel,
                                    borneViewModel = borneViewModel,
                                    carteId = carteId,
                                    showAlert = { message, isSuccess ->
                                        alertMessage = message
                                        alertIsSuccess = isSuccess
                                        alertVisible = true
                                    },
                                    containerColor = if (isSystemInDarkTheme()) DarkContainerColor else MaterialTheme.colorScheme.surface,
                                )
                            } ?: Text(text = "Aucune borne disponible pour cette carte.")
                        }
                    }
                }
                if (alertVisible) {
                    Alert(
                        isSuccess = alertIsSuccess,
                        message = alertMessage,
                        onDismiss = { alertVisible = false }
                    )
                }
                Menu(
                    navController = navController,
                    isMenuOpen = isMenuOpen,
                    onToggleMenu = { isMenuOpen = !isMenuOpen },
                    currentPage = "userHome",
                    userId = userId,
                    userViewModel = userViewModel
                )
            }
        )
    }
}
