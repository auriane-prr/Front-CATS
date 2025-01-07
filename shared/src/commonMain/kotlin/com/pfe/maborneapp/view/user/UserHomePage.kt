package com.pfe.maborneapp.view.user

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pfe.maborneapp.view.components.NetworkImage
import com.pfe.maborneapp.view.user.components.BorneList
import com.pfe.maborneapp.viewmodel.CarteViewModel
import com.pfe.maborneapp.viewmodel.factories.CarteViewModelFactory
import com.pfe.maborneapp.view.user.components.Menu
import com.pfe.maborneapp.viewmodel.factories.BorneViewModelFactory
import com.pfe.maborneapp.viewmodel.factories.SignalementViewModelFactory
import com.pfe.maborneapp.viewmodel.factories.user.UserViewModelFactory
import com.pfe.maborneapp.viewmodel.BorneViewModel
import com.pfe.maborneapp.viewmodel.user.UserViewModel
import com.pfe.maborneapp.viewmodel.SignalementViewModel

@Composable
fun UserHomePage(navController: NavHostController, userId: String) {
    val userViewModel: UserViewModel = viewModel(factory = UserViewModelFactory())
    val userEmail by userViewModel.userEmail.collectAsState()

    val carteViewModel: CarteViewModel = viewModel(factory = CarteViewModelFactory())
    val selectedCarteImageUrl by carteViewModel.selectedCarteImageUrl.collectAsState()

    val borneViewModel: BorneViewModel = viewModel(factory = BorneViewModelFactory())
    val bornes by borneViewModel.bornes.collectAsState()

    val signalementViewModel: SignalementViewModel = viewModel(factory = SignalementViewModelFactory())

    val greenColor = Color(0xFF045C3C)
    var isMenuOpen by remember { mutableStateOf(false) }

    // Fetch email on component load
    LaunchedEffect(userId) {
        userViewModel.fetchUserEmail(userId)
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        content = {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxSize()
                ) {
                    // Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = if (userEmail.isNotEmpty()) "Bonjour $userEmail" else "Chargement...",
                            style = MaterialTheme.typography.titleLarge,
                            color = greenColor,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Titre de la carte
                    Text(
                        text = "CATS de Montpellier",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    NetworkImage(
                        imageUrl = selectedCarteImageUrl,
                        contentDescription = "Carte Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    bornes?.let {
                        BorneList(
                            bornes = it,
                            userId = userId,
                            signalementViewModel = signalementViewModel
                        )
                    } ?: run {
                        Text(text = "Chargement des bornes...")
                    }
                }

                // Menu avec gestion de l'icône
                Menu(
                    navController = navController,
                    isMenuOpen = isMenuOpen,
                    onToggleMenu = { isMenuOpen = !isMenuOpen },
                    currentPage = "home",
                    userId = userId
                )
            }
        }
    )
}

