package com.pfe.maborneapp.view.admin

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.pfe.maborneapp.view.admin.components.AdminMenu
import com.pfe.maborneapp.view.admin.components.SignalementList
import com.pfe.maborneapp.viewmodel.admin.SignalementViewModel
import com.pfe.maborneapp.viewmodel.factories.SignalementViewModelFactory

@Composable
fun AdminSignalementPage(navController: NavHostController) {
    val viewModel: SignalementViewModel = viewModel(factory = SignalementViewModelFactory())
    val signalements by viewModel.signalements.collectAsState()

    var isMenuOpen by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        content = { padding ->
            Box {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Signalements des bornes",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(16.dp)
                    )

                    signalements?.let {
                        SignalementList(signalements = it)
                    } ?: run {
                        CircularProgressIndicator()
                    }
                }

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
