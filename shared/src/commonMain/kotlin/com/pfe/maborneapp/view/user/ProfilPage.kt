package com.pfe.maborneapp.view.user

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.pfe.maborneapp.view.user.components.Menu

@Composable
fun ProfilPage(navController: NavHostController, userEmail: String) {
    var isMenuOpen by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        content = { padding ->
            Box {
                // Contenu principal
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Ceci est la page de profil",
                        style = MaterialTheme.typography.titleLarge
                    )
                }

                // Menu
                Menu(
                    navController = navController,
                    isMenuOpen = isMenuOpen,
                    onToggleMenu = { isMenuOpen = !isMenuOpen },
                    currentPage = "profil",
                    userEmail = userEmail
                )

            }
        }
    )
}
