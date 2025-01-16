package com.pfe.maborneapp.view.user.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import androidx.compose.ui.Alignment
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pfe.maborneapp.utils.DarkModeGreen
import com.pfe.maborneapp.viewmodel.factories.user.UserViewModelFactory
import com.pfe.maborneapp.viewmodel.user.UserViewModel

@Composable
fun Menu(
    navController: NavHostController,
    isMenuOpen: Boolean,
    onToggleMenu: () -> Unit,
    currentPage: String,
    userId: String
) {
    val userViewModel: UserViewModel = viewModel(factory = UserViewModelFactory())

    LaunchedEffect(userId) {
        userViewModel.fetchUserEmail(userId)
    }
    val darkModeColorGreen = if (isSystemInDarkTheme()) DarkModeGreen else Color(0xFF045C3C)
    val menuBackgroundColor = Color(0xFFBDD3D0)

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        if (isMenuOpen) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .wrapContentHeight()
                    .width(220.dp)
                    .background(menuBackgroundColor, RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp))
                    .padding(vertical = 8.dp, horizontal = 12.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    Spacer(modifier = Modifier.height(32.dp))

                    // Accueil
                    TextButton(onClick = { navController.navigate("userHome/$userId") }) {
                        Text(
                            text = "Accueil",
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (currentPage == "home") darkModeColorGreen else Color.Black
                        )
                    }
                    Divider(color = darkModeColorGreen, thickness = 1.dp)

                    // Réservations
                    TextButton(onClick = { navController.navigate("reservations/$userId") }) {
                        Text(
                            text = "Réservations",
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (currentPage == "reservations") darkModeColorGreen else Color.Black
                        )
                    }
                    Divider(color = darkModeColorGreen, thickness = 1.dp)
/*
                    // Profil
                    TextButton(onClick = { navController.navigate("profil/$userId") }) {
                        Text(
                            text = "Profil",
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (currentPage == "profil") darkModeColorGreen else Color.Black
                        )
                    }
                    Divider(color = darkModeColorGreen, thickness = 1.dp)*/

                    // Déconnexion
                    TextButton(onClick = {
                        navController.navigate("login") {
                            popUpTo("userHome") { inclusive = true }
                        }
                    }) {
                        Text(
                            text = "Déconnexion",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Black
                        )
                    }
                }
            }
        }

        // Icône du menu
        IconButton(
            onClick = { onToggleMenu() },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(horizontal = 16.dp)
                .padding(top = 24.dp)
                .zIndex(1f)
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menu",
                tint = darkModeColorGreen
            )
        }
    }
}
