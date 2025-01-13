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
import androidx.compose.ui.graphics.ImageBitmap
import com.pfe.maborneapp.utils.loadImageBitmap
import com.pfe.maborneapp.view.components.Alert

@Composable
fun LoginPage(navController: NavHostController) {
    println("DEBUG, LoginPage: Composable affiché")

    val viewModel: LoginViewModel = viewModel(factory = LoginViewModelFactory())

    var mail by remember { mutableStateOf("") }
    val userRole by viewModel.userRole.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    println("DEBUG,LoginPage: userRole=$userRole")

    val greenColor = Color(0xFF045C3C)
    var alertVisible by remember { mutableStateOf(false) }
    var alertMessage by remember { mutableStateOf("") }
    var alertIsSuccess by remember { mutableStateOf(true) }

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
            Column(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxSize()
                    .padding(horizontal = 32.dp),
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
                            .size(200.dp)
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
                            .height(400.dp)
                            .aspectRatio(it.width.toFloat() / it.height.toFloat())
                            .padding(bottom = 16.dp)
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
                        focusedBorderColor = greenColor,
                        unfocusedBorderColor = greenColor,
                        focusedLabelColor = greenColor
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Affichage du loader ou du bouton
                if (isLoading) {
                    CircularProgressIndicator(
                        color = greenColor,
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
                            containerColor = greenColor,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Connexion")
                    }
                }
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
