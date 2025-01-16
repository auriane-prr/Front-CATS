package com.pfe.maborneapp.view.admin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.pfe.maborneapp.models.CarteId
import com.pfe.maborneapp.utils.DarkContainerColor
import com.pfe.maborneapp.utils.DarkModeGreen
import com.pfe.maborneapp.view.admin.components.AdminMenu
import com.pfe.maborneapp.view.admin.components.BorneListAdmin
import com.pfe.maborneapp.view.components.image.NetworkImage
import com.pfe.maborneapp.view.components.image.ZoomableImageView
import com.pfe.maborneapp.viewmodel.factories.BorneViewModelFactory
import com.pfe.maborneapp.viewmodel.BorneViewModel
import com.pfe.maborneapp.view.components.CarteDropdownMenu
import com.pfe.maborneapp.viewmodel.LocalCarteViewModel

@Composable
fun AdminHomePage(navController: NavHostController) {
    val darkModeColorGreen = if (isSystemInDarkTheme()) DarkModeGreen else Color(0xFF045C3C)
    val carteViewModel = LocalCarteViewModel.current

    val borneViewModel: BorneViewModel = viewModel(factory = BorneViewModelFactory())

    // États pour gérer les cartes, les bornes, et les erreurs
    val cartes by carteViewModel.carte.collectAsState()
    val selectedCarteImageUrl by carteViewModel.selectedCarteImageUrl.collectAsState()
    val selectedCarteLastModified by carteViewModel.selectedCarteLastModified.collectAsState()
    val selectedCarte by carteViewModel.selectedCarte.collectAsState()
    val etatBornes by borneViewModel.etatBornes.collectAsState()
    val isLoadingCartes by carteViewModel.isLoading.collectAsState()
    val isLoadingBornes by borneViewModel.isLoading.collectAsState()
    val errorLoadingCartes by carteViewModel.errorMessage.collectAsState()

    // Gestion de la carte sélectionnée

    var isMenuOpen by remember { mutableStateOf(false) }
    var showZoomableMap by remember { mutableStateOf(false) }
    var isCreateModalOpen by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val carteId = CarteId(selectedCarte?.id ?: "")

    // Charger les cartes au montage
    LaunchedEffect(Unit) {
        carteViewModel.fetchCartes()
    }

    // Sélectionner la carte par défaut (CATS de Montpellier) après le chargement des cartes
    LaunchedEffect(cartes) {
        if (cartes.isNotEmpty() && selectedCarte == null) {
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


    if (showZoomableMap) {
        ZoomableImageView(
            imageUrl = selectedCarteImageUrl,
            lastModified = selectedCarteLastModified,
            contentDescription = "Carte détaillée",
            onClose = { showZoomableMap = false }
        )
    } else {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        selectedCarte?.let { carte ->
                            navController.navigate("newBorne/${selectedCarte?.id}")

                        }
                    },
                    containerColor = darkModeColorGreen,
                    contentColor = Color.White,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Nouvelle Borne")
                }
            },
                    content = { paddingValues ->
                        Column(
                            modifier = Modifier
                                .padding(paddingValues)
                                .padding(horizontal = 16.dp)
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                        ) {
                            Text(
                                text = "Bienvenue sur le tableau de bord administrateur",
                                style = MaterialTheme.typography.titleLarge,
                                color = darkModeColorGreen,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                            // Menu déroulant pour sélectionner une carte
                            if (isLoadingCartes) {
                                CircularProgressIndicator(
                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                    color = darkModeColorGreen)
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
                                    CircularProgressIndicator(
                                        modifier = Modifier.align(Alignment.CenterHorizontally),
                                        color = darkModeColorGreen)
                                } else {
                                    etatBornes?.let {
                                        Text(text = "Bornes associées :",
                                            fontSize = 20.sp)
                                        Spacer(modifier = Modifier.height(16.dp))
                                        BorneListAdmin(
                                            etatBornes = it,
                                            viewModel = borneViewModel,
                                            containerColor = if (isSystemInDarkTheme()) DarkContainerColor else MaterialTheme.colorScheme.surface,
                                        )
                                    } ?: Text(text = "Aucune borne disponible pour cette carte.")
                                }
                            }

                            if (isCreateModalOpen) {
                                NewBornePage(
                                    navController = navController,
                                    defaultCarteId = selectedCarte?.id ?: "",

                                )
                            }

                            // Afficher une alerte en cas d'erreur
                            errorMessage?.let { error ->
                                AlertDialog(
                                    onDismissRequest = { errorMessage = null },
                                    confirmButton = {
                                        Button(onClick = { errorMessage = null }) {
                                            Text("OK")
                                        }
                                    },
                                    title = { Text("Erreur") },
                                    text = { Text(error) }
                                )
                            }

                        }
                        AdminMenu(
                            navController = navController,
                            isMenuOpen = isMenuOpen,
                            onToggleMenu = { isMenuOpen = !isMenuOpen },
                            currentPage = "adminHome",
                            carteId = carteId
                        )
                    }
        )
    }
}
