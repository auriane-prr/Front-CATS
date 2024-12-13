package com.pfe.maborneapp.android.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.pfe.maborneapp.android.viewmodel.LoginViewModel
import com.pfe.maborneapp.android.viewmodel.factories.LoginViewModelFactory
import com.pfe.maborneapp.repositories.LoginRepository

@Composable
fun LoginPage(navController: NavHostController) {
    val viewModel: LoginViewModel = viewModel(factory = LoginViewModelFactory(LoginRepository()))

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val loginMessage by viewModel.loginMessage.collectAsState()
    val userRole by viewModel.userRole.collectAsState() // Nouvel état pour le rôle utilisateur
    var hasNavigated by remember { mutableStateOf(false) }

    LaunchedEffect(loginMessage) {
        if (loginMessage == "Connexion réussie !" && !hasNavigated) {
            hasNavigated = true
            when (userRole) {
                "admin" -> navController.navigate("adminHome") {
                    popUpTo("login") { inclusive = true }
                }
                "user" -> navController.navigate("userHome") {
                    popUpTo("login") { inclusive = true }
                }
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
                    onClick = { viewModel.login(email, password) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Se connecter")
                }
            }
        }
    )
}
