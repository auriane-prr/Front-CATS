package com.pfe.maborneapp.view.user

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.pfe.maborneapp.view.user.components.Menu
import com.pfe.maborneapp.viewmodel.factories.user.UserViewModelFactory
import com.pfe.maborneapp.viewmodel.user.UserViewModel

@Composable
fun ReservationPage(navController: NavHostController, userId: String) {
    val userViewModel: UserViewModel = viewModel(factory = UserViewModelFactory())
    val userEmail by userViewModel.userEmail.collectAsState()

    LaunchedEffect(userId) {
        userViewModel.fetchUserEmail(userId)
    }

    var isMenuOpen by remember { mutableStateOf(false) }
    val greenColor = Color(0xFF045C3C)

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

                    Text(
                        text = "Ceci est la page de réservation",
                        style = MaterialTheme.typography.titleLarge
                    )
                }

                // Menu
                Menu(
                    navController = navController,
                    isMenuOpen = isMenuOpen,
                    onToggleMenu = { isMenuOpen = !isMenuOpen },
                    currentPage = "reservations",
                    userId = userId
                )
            }
        }
    )
}
