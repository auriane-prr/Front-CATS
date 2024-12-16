package com.pfe.maborneapp.android

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pfe.maborneapp.HttpClientFactoryImpl // Nouvelle implémentation
import com.pfe.maborneapp.android.view.LoginPage
import com.pfe.maborneapp.android.view.admin.AdminHomePage
import com.pfe.maborneapp.android.view.user.UserHomePage

class MainActivity : ComponentActivity() {
    private val client = HttpClientFactoryImpl().create() // Utilisation de HttpClientFactoryImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("DEBUG", "MainActivity: Lancement de l'application")
        setContent {
            val navController = rememberNavController()
            AppNavigation(navController, client)
        }
    }
}

@Composable
fun AppNavigation(navController: NavHostController, client: io.ktor.client.HttpClient) {
    Log.e("DEBUG", "AppNavigation: Chargement de la navigation")
    NavHost(navController, startDestination = "login") {
        composable("login") { LoginPage(navController, client) }
        composable("adminHome") { AdminHomePage() }
        composable("userHome") { UserHomePage() }
    }
}
