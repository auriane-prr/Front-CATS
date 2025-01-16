package com.pfe.maborneapp.view.user

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pfe.maborneapp.utils.DarkContainerColor
import com.pfe.maborneapp.utils.DarkModeGreen
import com.pfe.maborneapp.view.components.image.MapView
import com.pfe.maborneapp.view.user.components.BorneList
import com.pfe.maborneapp.viewmodel.CarteViewModel
import com.pfe.maborneapp.view.user.components.Menu
import com.pfe.maborneapp.viewmodel.factories.BorneViewModelFactory
import com.pfe.maborneapp.viewmodel.factories.SignalementViewModelFactory
import com.pfe.maborneapp.viewmodel.factories.user.UserViewModelFactory
import com.pfe.maborneapp.viewmodel.BorneViewModel
import com.pfe.maborneapp.viewmodel.user.UserViewModel
import com.pfe.maborneapp.viewmodel.SignalementViewModel
import com.pfe.maborneapp.viewmodel.factories.CarteViewModelFactory
import com.pfe.maborneapp.view.components.Alert
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import com.pfe.maborneapp.models.CarteId
import com.pfe.maborneapp.view.admin.components.BorneListAdmin
import com.pfe.maborneapp.view.admin.components.CarteDropdownMenu
import com.pfe.maborneapp.view.components.image.NetworkImage
import com.pfe.maborneapp.view.components.image.ZoomableImageView
import com.pfe.maborneapp.viewmodel.LocalCarteViewModel

@Composable
fun UserHomePage(navController: NavHostController, userId: String, carteId: String? = null) {
    val darkModeColorTitle = if (isSystemInDarkTheme()) DarkModeGreen else Color(0xFF045C3C)
    val userViewModel: UserViewModel = viewModel(factory = UserViewModelFactory())
    val borneViewModel: BorneViewModel = viewModel(factory = BorneViewModelFactory())
    val signalementViewModel: SignalementViewModel = viewModel(factory = SignalementViewModelFactory())
    val carteViewModel = LocalCarteViewModel.current

    val userEmail by userViewModel.userEmail.collectAsState()
    val selectedCarteImageUrl by carteViewModel.selectedCarteImageUrl.collectAsState()
    val selectedCarteLastModified by carteViewModel.selectedCarteLastModified.collectAsState()
    val etatBornes by borneViewModel.etatBornes.collectAsState()
    val isLoading by borneViewModel.isLoading.collectAsState()
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

    // Charger les cartes au montage
    LaunchedEffect(Unit) {
        carteViewModel.fetchCartes()
    }

    // Sélectionner la carte par défaut (CATS de Montpellier) après le chargement des cartes
    LaunchedEffect(cartes) {
        if (!cartes.isNullOrEmpty() && selectedCarte == null) {
            carteViewModel.setSelectedCarte(cartes.find { it.nom == "CATS de Montpellier" })
        }
    }

    // Charger les détails de la carte sélectionnée
    LaunchedEffect(selectedCarte) {
        selectedCarte?.let {
            println("DEBUG: Chargement des bornes pour la carte sélectionnée - ID: ${it.id}")
            carteViewModel.fetchCarteDetails(it.id)
            borneViewModel.fetchBornesByEtatAndCarte(CarteId(it.id))
        } ?: println("DEBUG: Aucune carte sélectionnée")
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
                    Text(
                        text = if (userEmail.isNotEmpty()) "Bonjour $userEmail" else "Chargement...",
                        style = MaterialTheme.typography.titleLarge,
                        color = darkModeColorTitle,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    // Menu déroulant pour sélectionner une carte
                    if (isLoadingCartes) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    } else if (!cartes.isNullOrEmpty()) {
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
                            lastModified = selectedCarteLastModified,
                            contentDescription = "Carte Image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showZoomableMap = true }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        if (isLoadingBornes) {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                        } else {
                            etatBornes?.let {
                                Text(text = "Bornes associées :",
                                    fontSize = 20.sp)
                                Spacer(modifier = Modifier.height(16.dp))
                                BorneListAdmin(
                                    etatBornes = it,
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
                    currentPage = "home",
                    userId = userId
                )
            }
        )
    }
}
