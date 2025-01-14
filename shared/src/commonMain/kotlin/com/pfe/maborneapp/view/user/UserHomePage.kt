package com.pfe.maborneapp.view.user

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pfe.maborneapp.utils.DarkContainerColor
import com.pfe.maborneapp.utils.DarkModeGreen
import com.pfe.maborneapp.view.components.image.MapView
import com.pfe.maborneapp.view.user.components.BorneList
import com.pfe.maborneapp.viewmodel.CarteViewModel
import com.pfe.maborneapp.view.user.components.Menu
import com.pfe.maborneapp.viewmodel.factories.BorneViewModelFactory
import com.pfe.maborneapp.viewmodel.factories.SignalementViewModelFactory
import com.pfe.maborneapp.viewmodel.factories.user.UserViewModelFactory
import com.pfe.maborneapp.viewmodel.BorneViewModel
import com.pfe.maborneapp.viewmodel.user.UserViewModel
import com.pfe.maborneapp.viewmodel.SignalementViewModel
import com.pfe.maborneapp.viewmodel.factories.CarteViewModelFactory
import com.pfe.maborneapp.view.components.Alert
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import com.pfe.maborneapp.view.components.image.NetworkImage
import com.pfe.maborneapp.view.components.image.ZoomableImageView

@Composable
fun UserHomePage(navController: NavHostController, userId: String, carteId: String? = null) {
    val darkModeColorTitle = if (isSystemInDarkTheme()) DarkModeGreen else Color(0xFF045C3C)
    val userViewModel: UserViewModel = viewModel(factory = UserViewModelFactory())
    val userEmail by userViewModel.userEmail.collectAsState()

    val carteViewModel: CarteViewModel = viewModel(factory = CarteViewModelFactory())
    val selectedCarteImageUrl by carteViewModel.selectedCarteImageUrl.collectAsState()
    val selectedCarteLastModified by carteViewModel.selectedCarteLastModified.collectAsState()

    val borneViewModel: BorneViewModel = viewModel(factory = BorneViewModelFactory())
    val etatBornes by borneViewModel.etatBornes.collectAsState()
    val isLoading by borneViewModel.isLoading.collectAsState()

    val signalementViewModel: SignalementViewModel = viewModel(factory = SignalementViewModelFactory())

    var isMenuOpen by remember { mutableStateOf(false) }
    var alertVisible by remember { mutableStateOf(false) }
    var alertMessage by remember { mutableStateOf("") }
    var alertIsSuccess by remember { mutableStateOf(true) }
    var showZoomableMap by remember { mutableStateOf(false) }

    LaunchedEffect(userId, carteId) {
        userViewModel.fetchUserEmail(userId)
        carteViewModel.fetchCarteDetails(carteId)
        borneViewModel.fetchBornesByEtat()
    }

    if (showZoomableMap) {
        ZoomableImageView(
            imageUrl = selectedCarteImageUrl,
            lastModified = selectedCarteLastModified,
            contentDescription = "Detailed Map",
            onClose = { showZoomableMap = false }
        )
    } else {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            content = {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = if (userEmail.isNotEmpty()) "Bonjour $userEmail" else "Chargement...",
                        style = MaterialTheme.typography.titleLarge,
                        color = darkModeColorTitle,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "CATS de Montpellier",
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 20.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    NetworkImage(
                        imageUrl = selectedCarteImageUrl,
                        lastModified = selectedCarteLastModified,
                        contentDescription = "Carte Image",
                        modifier = Modifier.clickable { showZoomableMap = true }
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Bornes :",
                        fontSize = 20.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = darkModeColorTitle,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    } else {
                        etatBornes?.let {
                            if (it.disponible.isEmpty() && it.occupee.isEmpty() && it.hs.isEmpty() && it.signalee.isEmpty()) {
                                Text(text = "Aucune borne disponible pour le moment.", color = Color.Red)
                            } else {
                                BorneList(
                                    etatBornes = it,
                                    userId = userId,
                                    signalementViewModel = signalementViewModel,
                                    borneViewModel = borneViewModel,
                                    showAlert = { message, isSuccess ->
                                        alertMessage = message
                                        alertIsSuccess = isSuccess
                                        alertVisible = true
                                    },
                                    containerColor = if (isSystemInDarkTheme()) DarkContainerColor else MaterialTheme.colorScheme.surface,
                                )
                            }
                        }
                    }
                }
                if (alertVisible) {
                    Alert(
                        isSuccess = alertIsSuccess,
                        message = alertMessage,
                        onDismiss = { alertVisible = false }
                    )
                }
                Menu(
                    navController = navController,
                    isMenuOpen = isMenuOpen,
                    onToggleMenu = { isMenuOpen = !isMenuOpen },
                    currentPage = "home",
                    userId = userId
                )
            }
        )
    }
}
