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
import com.pfe.maborneapp.models.CarteId
import com.pfe.maborneapp.utils.*
import com.pfe.maborneapp.view.admin.components.BorneListAdmin
import com.pfe.maborneapp.view.components.CarteDropdownMenu
import com.pfe.maborneapp.view.components.image.*
import com.pfe.maborneapp.view.admin.components.*
import com.pfe.maborneapp.viewmodel.*

@Composable
fun AdminHomePage(
    navController: NavController,
    carteViewModel: CarteViewModel,
    borneViewModel: BorneViewModel,
    typeBorneViewModel: TypeBorneViewModel,
    userViewModel: UserViewModel
) {
    val darkModeColorGreen = if (isSystemInDarkTheme()) DarkModeGreen else Color(0xFF045C3C)

    // État des cartes et bornes
    val cartes by carteViewModel.carte.collectAsState()
    val selectedCarte by carteViewModel.selectedCarte.collectAsState()
    val etatBornes by borneViewModel.etatBornes.collectAsState()
    val isLoadingCartes by carteViewModel.isLoading.collectAsState()
    val isLoadingBornes by borneViewModel.isLoading.collectAsState()
    val selectedCarteImageUrl by carteViewModel.selectedCarteImageUrl.collectAsState()
    val selectedCarteLastModified by carteViewModel.selectedCarteLastModified.collectAsState()
    val errorLoadingCartes by carteViewModel.errorMessage.collectAsState()

    // État local
    var isMenuOpen by remember { mutableStateOf(false) }
    var showZoomableMap by remember { mutableStateOf(false) }
    var isCreateModalOpen by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Charger les cartes au montage
    LaunchedEffect(Unit) {
        carteViewModel.fetchCartes()
    }

    // Sélectionner la carte par défaut si aucune carte n'est sélectionnée
    LaunchedEffect(cartes) {
        if (cartes.isNotEmpty() && selectedCarte == null) {
            carteViewModel.setSelectedCarte(cartes.find { it.nom == "CATS de Montpellier" })
        }
    }

    // Charger les bornes pour la carte sélectionnée
    LaunchedEffect(selectedCarte) {
        selectedCarte?.let {
            borneViewModel.fetchBornesByEtatAndCarte(CarteId(it.id))
            carteViewModel.fetchCarteDetails(it.id)
        }
    }

    // UI principale
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
                        navController.navigate("newBorne")
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
                    // Titre
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
                            color = darkModeColorGreen
                        )
                    } else if (cartes.isNotEmpty()) {
                        CarteDropdownMenu(
                            cartes = cartes,
                            selectedCarte = selectedCarte,
                            onCarteSelected = { carte ->
                                carteViewModel.setSelectedCarte(carte)
                                borneViewModel.fetchBornesByEtatAndCarte(CarteId(carte.id))
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
                            contentDescription = "Carte Image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showZoomableMap = true }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Affichage des bornes associées
                        if (isLoadingBornes) {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                color = darkModeColorGreen
                            )
                        } else {
                            etatBornes?.let { bornes ->
                                Text(
                                    text = "Bornes :",
                                    fontSize = 18.sp
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                BorneListAdmin(
                                    etatBornes = bornes,
                                    viewModel = borneViewModel,
                                    selectedCarteId = selectedCarte?.id ?: "",
                                    containerColor = if (isSystemInDarkTheme()) DarkContainerColor else MaterialTheme.colorScheme.surface
                                )
                            } ?: Text(text = "Aucune borne disponible pour cette carte.")
                        }
                    }
                    if (isCreateModalOpen) {
                        NewBornePage(
                            navController = navController,
                            defaultCarteId = selectedCarte?.id ?: "",
                            borneViewModel = borneViewModel,
                            carteViewModel = carteViewModel,
                            typeBorneViewModel = typeBorneViewModel
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
                    carteViewModel = carteViewModel,
                    userViewModel = userViewModel
                )
            }
        )
    }
}
