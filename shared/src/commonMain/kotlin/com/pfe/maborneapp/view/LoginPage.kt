package com.pfe.maborneapp.view

import androidx.lifecycle.viewmodel.compose.viewModel
import com.pfe.maborneapp.viewmodel.LoginViewModel
import com.pfe.maborneapp.viewmodel.factories.LoginViewModelFactory
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.graphics.ImageBitmap
import com.pfe.maborneapp.utils.DarkModeGreen
import com.pfe.maborneapp.utils.loadImageBitmap
import com.pfe.maborneapp.view.components.Alert
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

@Composable
fun LoginPage(navController: NavHostController) {
    println("DEBUG, LoginPage: Composable affiché")

    val viewModel: LoginViewModel = viewModel(factory = LoginViewModelFactory())

    var mail by remember { mutableStateOf("") }
    val userRole by viewModel.userRole.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var alertVisible by remember { mutableStateOf(false) }
    var alertMessage by remember { mutableStateOf("") }
    var alertIsSuccess by remember { mutableStateOf(true) }

    val darkModeColorGreen = if (isSystemInDarkTheme()) DarkModeGreen else Color(0xFF045C3C)

    LaunchedEffect(userRole) {
        if (userRole.isNotEmpty()) {
            val userId = viewModel.userId.value
            val route = if (userRole == "Admin") "adminHome" else "userHome/$userId"
            navController.navigate(route) {
                popUpTo("login") { inclusive = true }
            }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        content = { padding ->
            // Ajout du scroll state
            val scrollState = rememberScrollState()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Affichage du logo
                val logoImage by produceState<ImageBitmap?>(initialValue = null) {
                    value = loadImageBitmap("res://images/logo")
                }

                logoImage?.let {
                    Image(
                        bitmap = it,
                        contentDescription = "Logo",
                        modifier = Modifier
                            .size(180.dp)
                    )
                }

                // Affichage de l'image
                val loginImage by produceState<ImageBitmap?>(initialValue = null) {
                    value = loadImageBitmap("res://images/loginImage")
                }

                loginImage?.let {
                    Image(
                        bitmap = it,
                        contentDescription = "Login Illustration",
                        modifier = Modifier
                            .height(450.dp)
                            .aspectRatio(it.width.toFloat() / it.height.toFloat())
                            .padding(bottom = 24.dp)
                    )
                }

                // Champ d'email
                OutlinedTextField(
                    value = mail,
                    onValueChange = { mail = it },
                    label = { Text("Adresse mail", fontSize = 16.sp) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Email
                    ),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = darkModeColorGreen,
                        unfocusedBorderColor = darkModeColorGreen,
                        focusedLabelColor = darkModeColorGreen
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Affichage du loader ou du bouton
                if (isLoading) {
                    CircularProgressIndicator(
                        color = darkModeColorGreen,
                        modifier = Modifier.size(48.dp)
                    )
                } else {
                    Button(
                        onClick = {
                            viewModel.login(mail) { message, isSuccess ->
                                alertMessage = message
                                alertIsSuccess = isSuccess
                                alertVisible = true
                            }
                        },
                        modifier = Modifier
                            .height(48.dp)
                            .fillMaxWidth(),
                        enabled = mail.isNotEmpty(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = darkModeColorGreen,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Connexion")
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }

            // Alerte
            if (alertVisible) {
                Alert(
                    isSuccess = alertIsSuccess,
                    message = alertMessage,
                    onDismiss = { alertVisible = false }
                )
            }
        }
    )
}