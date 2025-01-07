package com.pfe.maborneapp

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pfe.maborneapp.utils.HttpClientFactoryImpl
import com.pfe.maborneapp.view.admin.AdminHomePage
import com.pfe.maborneapp.view.user.UserHomePage
import com.pfe.maborneapp.view.LoginPage
import com.pfe.maborneapp.view.admin.AdminSignalementPage
import com.pfe.maborneapp.view.admin.AdminStatistiquePage
import com.pfe.maborneapp.view.user.ReservationPage
import com.pfe.maborneapp.view.user.ProfilPage


@Composable
fun App() {
    MaterialTheme {
        val client = HttpClientFactoryImpl().create()

        val navController = rememberNavController()
        AppNavigation(navController)
    }
}

@Composable
fun AppNavigation(navController: NavHostController) {
    println("DEBUG, AppNavigation: Chargement de la navigation")
    NavHost(navController, startDestination = "login") {
        composable("login") { LoginPage(navController) }
        // Pages Admin
        composable("adminHome") { AdminHomePage(navController) }
        composable("adminSignalement") { AdminSignalementPage(navController) }
        composable("adminStatistique") { AdminStatistiquePage(navController) }
        composable("userHome/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            UserHomePage(navController, userId)
        }
        composable("reservations/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            ReservationPage(navController, userId)
        }
        composable("profil/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            ProfilPage(navController, userId)
        }
    }
}
