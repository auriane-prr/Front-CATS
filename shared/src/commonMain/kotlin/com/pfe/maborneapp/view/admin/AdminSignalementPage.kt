package com.pfe.maborneapp.view.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.pfe.maborneapp.view.admin.components.AdminMenu
import com.pfe.maborneapp.view.admin.components.SignalementList
import com.pfe.maborneapp.viewmodel.SignalementViewModel
import com.pfe.maborneapp.viewmodel.factories.SignalementViewModelFactory

@Composable
fun AdminSignalementPage(navController: NavHostController) {
    val viewModel: SignalementViewModel = viewModel(factory = SignalementViewModelFactory())
    val signalements by viewModel.signalements.collectAsState()
    var isMenuOpen by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) } // État pour le chargement initial
    var isActionLoading by remember { mutableStateOf(false) } // État pour les actions
    var errorMessage by remember { mutableStateOf<String?>(null) } // Gestion des erreurs

    // Charger les signalements au lancement
    LaunchedEffect(Unit) {
        try {
            viewModel.fetchSignalements()
            isLoading = false
        } catch (e: Exception) {
            errorMessage = "Erreur lors du chargement des signalements"
            isLoading = false
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        content = { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Signalements des bornes",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(16.dp)
                    )

                    when {
                        isLoading -> {
                            // Loader principal pendant le chargement initial
                            CircularProgressIndicator(
                                modifier = Modifier.padding(16.dp),
                                color = Color(0xFF045C3C)
                            )
                        }
                        errorMessage != null -> {
                            // Message d'erreur en cas de problème
                            Text(
                                text = errorMessage ?: "Une erreur inconnue s'est produite",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                        signalements != null -> {
                            // Affichage des signalements avec gestion des actions
                            SignalementList(
                                signalements = signalements!!,
                                onCloseSignalement = { signalementId ->
                                    isActionLoading = true // Démarrer le loader d'action
                                    viewModel.closeSignalement(
                                        signalementId = signalementId,
                                        onSuccess = {
                                            isActionLoading = false // Arrêter le loader d'action
                                            println("Signalement fermé avec succès.")
                                            viewModel.fetchSignalements() // Rafraîchir la liste
                                        },
                                        onError = { error ->
                                            isActionLoading = false
                                            println("Erreur lors de la fermeture du signalement : $error")
                                        }
                                    )
                                },
                                onUpdateStatus = { borneId, newStatus ->
                                    isActionLoading = true // Démarrer le loader d'action
                                    viewModel.updateBorneStatus(
                                        borneId = borneId,
                                        newStatus = newStatus,
                                        onSuccess = {
                                            isActionLoading = false // Arrêter le loader d'action
                                            println("Statut mis à jour avec succès.")
                                            viewModel.fetchSignalements() // Rafraîchir la liste
                                        },
                                        onError = { error ->
                                            isActionLoading = false
                                            println("Erreur lors de la mise à jour du statut : $error")
                                        }
                                    )
                                }
                            )
                        }
                        else -> {
                            // Message si aucun signalement disponible
                            Text(
                                text = "Aucun signalement disponible",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }

                // Afficher le loader d'action global (fond semi-transparent + CircularProgressIndicator)
                if (isActionLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = Color(0xFF045C3C),
                            strokeWidth = 4.dp
                        )
                    }
                }

                // Menu Admin
                AdminMenu(
                    navController = navController,
                    isMenuOpen = isMenuOpen,
                    onToggleMenu = { isMenuOpen = !isMenuOpen },
                    currentPage = "adminSignalement"
                )
            }
        }
    )
}
