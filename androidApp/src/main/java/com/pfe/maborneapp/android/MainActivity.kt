package com.pfe.maborneapp.android

import android.os.Bundle
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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            AppNavigation(navController)
        }
    }
}

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController, startDestination = "login") {
        composable("login") { LoginPage(navController) }
        composable("adminHome") { AdminHomePage() }
        composable("userHome") { UserHomePage() }
    }
}
