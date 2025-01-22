package com.pfe.maborneapp.view.admin

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.pfe.maborneapp.models.CarteId
import com.pfe.maborneapp.utils.NavController
import com.pfe.maborneapp.view.admin.components.AdminMenu
import com.pfe.maborneapp.viewmodel.CarteViewModel
import com.pfe.maborneapp.viewmodel.UserViewModel

@Composable
fun AdminStatistiquePage(navController: NavController, userViewModel: UserViewModel, carteViewModel: CarteViewModel) {
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
                        text = "Ceci est la page Statistique",
                        style = MaterialTheme.typography.titleLarge
                    )
                }

                // Menu Admin
                AdminMenu(
                    navController = navController,
                    isMenuOpen = isMenuOpen,
                    onToggleMenu = { isMenuOpen = !isMenuOpen },
                    currentPage = "adminStatistique",
                    userViewModel = userViewModel,
                    carteViewModel = carteViewModel
                )
            }
        }
    )
}

