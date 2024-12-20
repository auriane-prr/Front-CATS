package com.pfe.maborneapp

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pfe.maborneapp.view.admin.AdminHomePage
import com.pfe.maborneapp.view.user.UserHomePage
import com.pfe.maborneapp.view.LoginPage
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
        composable("adminHome") { AdminHomePage() }
        composable("userHome/{email}") { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            UserHomePage(navController, email)
        }
        composable("reservations/{email}") { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            ReservationPage(navController, email)
        }
        composable("profil/{email}") { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            ProfilPage(navController, email)
        }

    }
}
