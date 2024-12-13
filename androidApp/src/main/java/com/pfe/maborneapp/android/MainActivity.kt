package com.pfe.maborneapp.android

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import androidx.navigation.NavHostController
import com.pfe.maborneapp.LoginRepository

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("[DEBUG]", "Application started")
        Log.e("Test", "Application started")
        setContent {
            val navController = rememberNavController()
            NavHost(navController, startDestination = "login") {
                composable("login") { LoginPage(navController) }
                composable("home") { HomePage() }
            }
        }
    }
}

@Composable
fun LoginPage(navController: NavHostController) {
    val viewModel: LoginViewModel = viewModel(factory = LoginViewModelFactory(LoginRepository()))

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val loginMessage by viewModel.loginMessage.collectAsState()
    var hasNavigated by remember { mutableStateOf(false) }

    // Debug : affichage initial des valeurs
    Log.d("[DEBUG]","Initial loginMessage: $loginMessage")
    Log.d("[DEBUG]","Initial hasNavigated: $hasNavigated")

    LaunchedEffect(loginMessage) {
        Log.e("[DEBUG]", "LaunchedEffect triggered with loginMessage=$loginMessage")
        if (loginMessage == "Connexion réussie !" && !hasNavigated) {
            Log.e("[DEBUG]", "Navigating to Home")
            hasNavigated = true
            navController.navigate("home") {
                popUpTo("login") { inclusive = true }
            }
        }
    }


    Scaffold(
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Mot de passe") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        Log.e("[DEBUG]", "Button clicked. Email=$email, Password=$password")
                        viewModel.login(email, password)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Se connecter")
                }

            }
        }
    )
}

@Composable
fun HomePage() {
    Log.e("[DEBUG]", "HomePage displayed")
    Text("Bonjour, vous êtes connecté !")
}
