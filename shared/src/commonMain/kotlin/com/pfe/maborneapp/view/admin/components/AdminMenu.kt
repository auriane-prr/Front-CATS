package com.pfe.maborneapp.view.admin.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import androidx.compose.ui.Alignment

@Composable
fun AdminMenu(
    navController: NavHostController,
    isMenuOpen: Boolean,
    onToggleMenu: () -> Unit,
    currentPage: String
) {
    val greenColor = Color(0xFF045C3C)
    val menuBackgroundColor = Color(0xFFBDD3D0)

    Box(
        modifier = Modifier.fillMaxSize() // Parent qui prend toute la taille de l'écran
    ) {
        if (isMenuOpen) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd) // Place le menu en haut à droite
                    .height(250.dp)
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
                    TextButton(onClick = { navController.navigate("adminHome") }) {
                        Text(
                            text = "Accueil",
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (currentPage == "adminHome") greenColor else Color.Black
                        )
                    }
                    Divider(color = greenColor, thickness = 1.dp)

                    // Signalement
                    TextButton(onClick = { navController.navigate("adminSignalement") }) {
                        Text(
                            text = "Signalement",
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (currentPage == "adminSignalement") greenColor else Color.Black
                        )
                    }
                    Divider(color = greenColor, thickness = 1.dp)

                    // Statistique
                    TextButton(onClick = { navController.navigate("adminStatistique") }) {
                        Text(
                            text = "Statistique",
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (currentPage == "adminStatistique") greenColor else Color.Black
                        )
                    }
                    Divider(color = greenColor, thickness = 1.dp)

                    // Déconnexion
                    TextButton(onClick = { navController.navigate("login") }) {
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
                .align(Alignment.TopEnd) // Place l'icône dans le coin supérieur droit
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .zIndex(1f)
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menu",
                tint = greenColor
            )
        }
    }
}

