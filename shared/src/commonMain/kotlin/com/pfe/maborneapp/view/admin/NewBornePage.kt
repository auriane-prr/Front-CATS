package com.pfe.maborneapp.view.admin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.pfe.maborneapp.models.Carte
import com.pfe.maborneapp.models.CarteId
import com.pfe.maborneapp.models.CreateBorneRequest
import com.pfe.maborneapp.models.TypeBorne
import com.pfe.maborneapp.models.TypeBorneId
import com.pfe.maborneapp.utils.DarkModeGreen
import com.pfe.maborneapp.view.components.CarteDropdownMenu
import com.pfe.maborneapp.view.admin.components.CustomDropDown
import com.pfe.maborneapp.view.components.Alert
import com.pfe.maborneapp.view.components.image.NetworkImage
import com.pfe.maborneapp.view.components.image.ZoomableImageView
import com.pfe.maborneapp.viewmodel.BorneViewModel
import com.pfe.maborneapp.viewmodel.LocalCarteViewModel
import com.pfe.maborneapp.viewmodel.TypeBorneViewModel
import com.pfe.maborneapp.viewmodel.factories.BorneViewModelFactory
import com.pfe.maborneapp.viewmodel.factories.TypeBorneViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewBornePage(navController: NavHostController, defaultCarteId: String) {
    val borneViewModel: BorneViewModel = viewModel(factory = BorneViewModelFactory())
    val carteViewModel = LocalCarteViewModel.current
    val typeBorneViewModel: TypeBorneViewModel = viewModel(factory = TypeBorneViewModelFactory())

    var coordX by remember { mutableStateOf("") }
    var coordY by remember { mutableStateOf("") }
    var numero by remember { mutableStateOf("") }
    var selectedCarte by remember { mutableStateOf<Carte?>(null) }
    var selectedTypeBorne by remember { mutableStateOf<TypeBorne?>(null) }
    var isSubmitting by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showZoomableMap by remember { mutableStateOf(false) }

    val cartes by carteViewModel.carte.collectAsState()
    val typesBorne by typeBorneViewModel.typesBorne.collectAsState()
    val isLoadingCartes by carteViewModel.isLoading.collectAsState()
    val isLoadingTypesBorne by typeBorneViewModel.isLoading.collectAsState()
    val selectedCarteImageUrl by carteViewModel.selectedCarteImageUrl.collectAsState()
    val selectedCarteLastModified by carteViewModel.selectedCarteLastModified.collectAsState()
    val creationStatus by borneViewModel.creationStatus.collectAsState()
    val errorMessage by borneViewModel.errorMessage.collectAsState()

    val darkModeColorGreen = if (isSystemInDarkTheme()) DarkModeGreen else Color(0xFF045C3C)

    LaunchedEffect(Unit) {
        carteViewModel.fetchCartes()
    }

    LaunchedEffect(defaultCarteId, cartes) {
        if (!cartes.isNullOrEmpty() && selectedCarte == null) {
            val carte = cartes.find { it.id == defaultCarteId }
            selectedCarte = carte
            carte?.let { carteViewModel.fetchCarteDetails(it.id) }
        }
    }

    LaunchedEffect(selectedCarte) {
        selectedCarte?.let { typeBorneViewModel.fetchTypesBorne() }
    }

    if (showZoomableMap) {
        ZoomableImageView(
            imageUrl = selectedCarteImageUrl,
            lastModified = selectedCarteLastModified,
            contentDescription = "Carte détaillée",
            onClose = { showZoomableMap = false }
        )
    } else {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                TopAppBar(
                    title = { Text("Nouvelle Borne") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                        }
                    }
                )
            },
            content = { paddingValues ->
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(16.dp)
                        .fillMaxSize()
                ) {
                    if (isLoadingCartes) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            color = darkModeColorGreen
                        )
                    } else if (!cartes.isNullOrEmpty()) {
                        CarteDropdownMenu(
                            cartes = cartes,
                            selectedCarte = selectedCarte,
                            onCarteSelected = { selectedCarte = it }
                        )
                    } else {
                        Text(
                            "Erreur lors du chargement des cartes.",
                            color = Color.Red,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    NetworkImage(
                        imageUrl = selectedCarteImageUrl,
                        lastModified = selectedCarteLastModified,
                        contentDescription = "Carte Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clickable { showZoomableMap = true }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    OutlinedTextField(
                        value = numero,
                        onValueChange = { numero = it },
                        label = { Text("Numéro de la borne") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = darkModeColorGreen,
                            unfocusedBorderColor = darkModeColorGreen,
                            focusedLabelColor = darkModeColorGreen
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (isLoadingTypesBorne) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            color = darkModeColorGreen
                        )
                    } else if (!typesBorne.isNullOrEmpty()) {
                        CustomDropDown(
                            typesBorne = typesBorne,
                            selectedType = selectedTypeBorne,
                            onTypeSelected = { selectedTypeBorne = it }
                        )
                    } else {
                        Text(
                            "Erreur lors du chargement des types de borne.",
                            color = Color.Red,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (numero.isNotEmpty() && selectedTypeBorne != null && selectedCarte != null) {
                                borneViewModel.createBorne(
                                    CreateBorneRequest(
                                        coord_x = coordX.toIntOrNull(),
                                        coord_y = coordY.toIntOrNull(),
                                        numero = numero.toInt(),
                                        typeBorne = TypeBorneId(selectedTypeBorne?.id ?: ""),
                                        carte = CarteId(selectedCarte?.id ?: "")
                                    )
                                )
                                isSubmitting = true
                            }
                        },
                        enabled = numero.isNotEmpty() && selectedTypeBorne != null && !isSubmitting,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = if (isSubmitting) Color.Gray else darkModeColorGreen),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = if (isSubmitting) "En cours..." else "Créer",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                }
            }
        )
    }

    if (showSuccessDialog) {
        Alert(
            isSuccess = true,
            message = "La borne a été créée avec succès.",
            onDismiss = {
                showSuccessDialog = false
                navController.navigate("adminHome") { popUpTo("adminHome") { inclusive = true } }
            }
        )
    }

    LaunchedEffect(creationStatus) {
        if (creationStatus == true) {
            showSuccessDialog = true
        } else if (creationStatus == false) {
            isSubmitting = false
        }
    }

    errorMessage?.let { message ->
        Alert(
            isSuccess = false,
            message = message,
            onDismiss = { borneViewModel.resetErrorMessage() }
        )
    }
}

