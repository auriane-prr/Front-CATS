package com.pfe.maborneapp.view.user

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.ui.zIndex
import com.pfe.maborneapp.view.user.components.*

@Composable
fun UserHomePage(navController: NavHostController, userEmail: String) {

    val cities = listOf("CA - Montpellier Sud", "CA - Lyon Centre", "CA - Paris Nord")
    var isMenuOpen by remember { mutableStateOf(false) }
    val greenColor = Color(0xFF045C3C)

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        content = { padding ->
            Box {
                // Contenu principal
                Column(
                    modifier = Modifier
                        .padding(padding)
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
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }

                    // Sélecteur de ville
                    DropdownMenu(cities = cities)

                    // Rectangle pour la carte
                    Spacer(modifier = Modifier.height(16.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .background(Color.LightGray, RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Carte à venir",
                            color = Color.Gray
                        )
                    }

                    // Rectangle pour les légendes
                    Spacer(modifier = Modifier.height(16.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .background(Color.LightGray, RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Légendes à venir",
                            color = Color.Gray
                        )
                    }
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

@Composable
fun DropdownMenu(cities: List<String>) {
    var expanded by remember { mutableStateOf(false) }
    var selectedCity by remember { mutableStateOf(cities.first()) }

    val greenColor = Color(0xFF045C3C)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = greenColor
            ),
            border = BorderStroke(1.dp, greenColor)
        ) {
            Text(text = selectedCity, color = greenColor)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            cities.forEach { city ->
                DropdownMenuItem(
                    text = { Text(city) },
                    onClick = {
                        selectedCity = city
                        expanded = false
                    }
                )
            }
        }
    }
}
