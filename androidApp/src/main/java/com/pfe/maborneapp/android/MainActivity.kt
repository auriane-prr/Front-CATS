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
import com.pfe.maborneapp.android.view.LoginPage
import com.pfe.maborneapp.android.view.admin.AdminHomePage
import com.pfe.maborneapp.android.view.user.UserHomePage
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*

class MainActivity : ComponentActivity() {
    private val client = HttpClient(OkHttp) {
        // Configuration du client HTTP
        install(ContentNegotiation) {
            json() // Gestion JSON avec Kotlinx Serialization
        }
        engine {
            config {
                retryOnConnectionFailure(true)
                connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            }
        }
        install(Logging) {
            level = LogLevel.BODY // Logs des requêtes et réponses
        }
    }

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
fun AppNavigation(navController: NavHostController, client: HttpClient) {
    Log.e("DEBUG", "AppNavigation: Chargement de la navigation")
    NavHost(navController, startDestination = "login") {
        composable("login") { LoginPage(navController, client) }
        composable("adminHome") { AdminHomePage() }
        composable("userHome") { UserHomePage() }
    }
}
