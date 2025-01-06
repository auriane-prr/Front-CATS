package com.pfe.maborneapp.view.admin

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.pfe.maborneapp.view.admin.components.AdminMenu

@Composable
fun AdminSignalementPage(navController: NavHostController) {
    var isMenuOpen by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        content = { padding ->
            Box {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Ceci est la page Signalement",
                        style = MaterialTheme.typography.titleLarge
                    )
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



