package com.pfe.maborneapp

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pfe.maborneapp.models.TypeBorne
import com.pfe.maborneapp.utils.ImageCache
import com.pfe.maborneapp.utils.DarkThemeColors
import com.pfe.maborneapp.utils.LightThemeColors
import com.pfe.maborneapp.view.admin.AdminHomePage
import com.pfe.maborneapp.view.user.UserHomePage
import com.pfe.maborneapp.view.LoginPage
import com.pfe.maborneapp.view.admin.AdminSignalementPage
import com.pfe.maborneapp.view.admin.AdminStatistiquePage
import com.pfe.maborneapp.view.admin.NewBornePage
import com.pfe.maborneapp.view.user.AvailableBornesPage
import com.pfe.maborneapp.view.user.ReservationPage
import com.pfe.maborneapp.view.user.ProfilPage
import com.pfe.maborneapp.view.user.NewReservationPage


@Composable
fun App(context: Any? = null) {
    AppTheme {
        // Initialisation du cache
        LaunchedEffect(context) {
            if (context != null) {
                ImageCache.initialize(context)
            }
        }

        MaterialTheme {
        val navController = rememberNavController()
        AppNavigation(navController)
    } }
}
@Composable
fun AppTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) DarkThemeColors else LightThemeColors


    MaterialTheme(
        colorScheme = colors,
        content = content
    )
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
        // Pages Admin
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
        composable("newReservation/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            NewReservationPage(navController, userId)
        }
        composable("newBorne/{carteId}") { backStackEntry ->
            val carteId = backStackEntry.arguments?.getString("carteId")
            NewBornePage(navController = navController, carteId = carteId)
        }



        composable(
            route = "availableBornes/{startDate}/{endDate}/{userId}",
            arguments = listOf(
                navArgument("startDate") { type = NavType.StringType },
                navArgument("endDate") { type = NavType.StringType },
                navArgument("userId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val startDate = backStackEntry.arguments?.getString("startDate") ?: ""
            val endDate = backStackEntry.arguments?.getString("endDate") ?: ""
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            AvailableBornesPage(navController, startDate, endDate, userId)
        }

    }
}
