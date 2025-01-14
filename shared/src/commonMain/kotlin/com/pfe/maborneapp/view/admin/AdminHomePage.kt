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
import com.pfe.maborneapp.utils.DarkContainerColor
import com.pfe.maborneapp.utils.DarkModeGreen
import com.pfe.maborneapp.view.admin.components.AdminMenu
import com.pfe.maborneapp.view.admin.components.BorneListAdmin
import com.pfe.maborneapp.view.components.image.MapView
import com.pfe.maborneapp.view.components.image.NetworkImage
import com.pfe.maborneapp.view.components.image.ZoomableImageView
import com.pfe.maborneapp.view.admin.components.CreateBorneModal
import com.pfe.maborneapp.viewmodel.CarteViewModel
import com.pfe.maborneapp.viewmodel.factories.CarteViewModelFactory
import com.pfe.maborneapp.viewmodel.factories.BorneViewModelFactory
import com.pfe.maborneapp.viewmodel.BorneViewModel
import com.pfe.maborneapp.models.CreateBorneRequest
import com.pfe.maborneapp.models.TypeBorne
@Composable
fun AdminHomePage(navController: NavHostController, carteId: String? = null) {
    val carteViewModel: CarteViewModel = viewModel(factory = CarteViewModelFactory())
    val selectedCarteImageUrl by carteViewModel.selectedCarteImageUrl.collectAsState()
    val selectedCarteLastModified by carteViewModel.selectedCarteLastModified.collectAsState()

    val borneViewModel: BorneViewModel = viewModel(factory = BorneViewModelFactory())
    val etatBornes by borneViewModel.etatBornes.collectAsState()
    val isLoading by borneViewModel.isLoading.collectAsState()
    val darkModeColorGreen = if (isSystemInDarkTheme()) DarkModeGreen else Color(0xFF045C3C)

    var isMenuOpen by remember { mutableStateOf(false) }
    var showZoomableMap by remember { mutableStateOf(false) }
    var isCreateModalOpen by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) } // Pour gérer les erreurs

    // Simulation de types de borne (remplacer par un appel réel)
    val typesBorne = listOf(
        TypeBorne("675c2adb6fc93512a0050c43", "Voiture"),
        TypeBorne("675c2adb6fc93512a0050c44", "Vélo")
    )

    LaunchedEffect(carteId) {
        carteViewModel.fetchCarteDetails(carteId)
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
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {  navController.navigate("newBorne")},
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

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "CATS de Montpellier",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    NetworkImage(
                        imageUrl = selectedCarteImageUrl,
                        lastModified = selectedCarteLastModified,
                        contentDescription = "Carte Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showZoomableMap = true }
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Bornes :",
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    if (isLoading) {
                        CircularProgressIndicator(
                            color = darkModeColorGreen,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    } else {
                        etatBornes?.let {
                            BorneListAdmin(
                                etatBornes = it,
                                containerColor = if (isSystemInDarkTheme()) DarkContainerColor else MaterialTheme.colorScheme.surface,
                            )
                        } ?: Text(text = "Chargement des bornes...")
                    }
                }

                if (isCreateModalOpen) {
                    NewBornePage(
                        navController = navController,
                        typesBorne = typesBorne,
                        carteId = carteId,
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

                AdminMenu(
                    navController = navController,
                    isMenuOpen = isMenuOpen,
                    onToggleMenu = { isMenuOpen = !isMenuOpen },
                    currentPage = "adminHome"
                )
            }
        )
    }
}
