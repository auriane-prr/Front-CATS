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
    val carteViewModel: CarteViewModel = viewModel(factory = CarteViewModelFactory())
    val selectedCarteImageUrl by carteViewModel.selectedCarteImageUrl.collectAsState()

    val greenColor = Color(0xFF045C3C)

    var isMenuOpen by remember { mutableStateOf(false) }

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
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Bonjour $userEmail",
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
                            .fillMaxWidth() // Largeur maximale avec padding
                            .padding(horizontal = 16.dp) // Ajout d'un padding
                    )


                    Spacer(modifier = Modifier.height(16.dp))

                    /*
                    // Rectangle pour les bornes
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .background(Color.LightGray, RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Bornes à venir",
                            color = Color.Gray
                        )
                    }*/
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

