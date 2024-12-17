package com.pfe.maborneapp.view


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.pfe.maborneapp.viewmodel.LoginViewModel
import com.pfe.maborneapp.viewmodel.factories.LoginViewModelFactory
import io.ktor.client.*

@Composable
fun LoginPage(navController: NavHostController) {
    println("DEBUG, LoginPage: Composable affiché")

    val viewModel: LoginViewModel = viewModel(factory = LoginViewModelFactory())

    var mail by remember { mutableStateOf("") }
    val loginMessage by viewModel.loginMessage.collectAsState()
    val userRole by viewModel.userRole.collectAsState()

    println("DEBUG,LoginPage: loginMessage=$loginMessage, userRole=$userRole")

    LaunchedEffect(userRole) {
        if (userRole.isNotEmpty()) {
            val route = if (userRole == "Admin") "adminHome" else "userHome"
            println("DEBUG,LoginPage: Navigation vers $route")
            navController.navigate(route) {
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
                    value = mail,
                    onValueChange = { mail = it },
                    label = { Text("mail") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        println("DEBUG, LoginPage: Bouton cliqué, mail=$mail")
                        viewModel.login(mail)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = mail.isNotEmpty() // Désactive le bouton si le champ est vide
                ) {
                    Text("Se connecter")
                }
                Text(
                    text = loginMessage,
                    color = if (loginMessage == "Connexion réussie !") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                )
            }
        }
    )
}
