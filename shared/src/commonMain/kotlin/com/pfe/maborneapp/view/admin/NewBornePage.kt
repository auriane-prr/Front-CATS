package com.pfe.maborneapp.view.admin
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.pfe.maborneapp.models.Carte
import com.pfe.maborneapp.models.CarteId
import com.pfe.maborneapp.models.CreateBorneRequest
import com.pfe.maborneapp.models.TypeBorne
import com.pfe.maborneapp.models.TypeBorneId
import com.pfe.maborneapp.utils.DarkModeGreen
import com.pfe.maborneapp.view.admin.components.CarteDropdownMenu
import com.pfe.maborneapp.view.admin.components.CustomDropDown
import com.pfe.maborneapp.view.components.image.NetworkImage
import com.pfe.maborneapp.view.components.image.ZoomableImageView
import com.pfe.maborneapp.viewmodel.BorneViewModel
import com.pfe.maborneapp.viewmodel.CarteViewModel
import com.pfe.maborneapp.viewmodel.LocalCarteViewModel
import com.pfe.maborneapp.viewmodel.TypeBorneViewModel
import com.pfe.maborneapp.viewmodel.factories.BorneViewModelFactory
import com.pfe.maborneapp.viewmodel.factories.CarteViewModelFactory
import com.pfe.maborneapp.viewmodel.factories.TypeBorneViewModelFactory


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewBornePage(navController: NavHostController, defaultCarteId: String) {
    val borneViewModel: BorneViewModel = viewModel(factory = BorneViewModelFactory())
    val carteViewModel = LocalCarteViewModel.current
    val typeBorneViewModel: TypeBorneViewModel = viewModel(factory = TypeBorneViewModelFactory())

    // États pour les champs de la borne et les cartes
    var coordX by remember { mutableStateOf("") }
    var coordY by remember { mutableStateOf("") }
    var numero by remember { mutableStateOf("") }
    var selectedCarte by remember { mutableStateOf<Carte?>(null) }
    var selectedTypeBorne by remember { mutableStateOf<TypeBorne?>(null) }
    var isSubmitting by remember { mutableStateOf(false) }
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
    // Charger les cartes au montage
    LaunchedEffect(Unit) {
        carteViewModel.fetchCartes()
    }

    // Sélectionner la carte par défaut avec l'ID passé
    LaunchedEffect(defaultCarteId, cartes) {
        if (!cartes.isNullOrEmpty() && selectedCarte == null) {
            val carte = cartes.find { it.id == defaultCarteId }
            selectedCarte = carte
            carte?.let { carteViewModel.fetchCarteDetails(it.id) }
        }
    }

    // Charger les types de bornes pour la carte sélectionnée
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
                    // Menu déroulant pour sélectionner une carte
                    if (isLoadingCartes) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    } else if (!cartes.isNullOrEmpty()) {
                        CarteDropdownMenu(
                            cartes = cartes,
                            selectedCarte = selectedCarte,
                            onCarteSelected = { selectedCarte = it }
                        )
                    } else {
                        Text(
                            text = "Erreur lors du chargement des cartes.",
                            color = Color.Red,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Spacer(modifier = Modifier.height(16.dp))

                    // Carte interactive
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

                    // Champ Numéro
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

                    // Menu déroulant pour les types de bornes
                    if (isLoadingTypesBorne) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    } else if (!typesBorne.isNullOrEmpty()) {
                        CustomDropDown(
                            typesBorne = typesBorne,
                            selectedType = selectedTypeBorne,
                            onTypeSelected = { selectedTypeBorne = it }
                        )
                    } else {
                        Text(
                            text = "Erreur lors du chargement des types de borne.",
                            color = Color.Red,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Bouton de soumission
                    Button(
                        onClick = {
                            if (numero.isNotEmpty() && selectedTypeBorne != null && selectedCarte != null) {
                                isSubmitting = true
                                borneViewModel.createBorne(
                                    CreateBorneRequest(
                                        coord_x = coordX.toIntOrNull(),
                                        coord_y = coordY.toIntOrNull(),
                                        numero = numero.toInt(),
                                        typeBorne = TypeBorneId(selectedTypeBorne?.id ?: ""),
                                        carte = CarteId(selectedCarte?.id ?: "")
                                    )
                                )
                            }
                        },
                        enabled = numero.isNotEmpty() && selectedTypeBorne != null && !isSubmitting,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = darkModeColorGreen)
                    ) {
                        Text(
                            text = if (isSubmitting) "En cours..." else "Créer",
                            color = Color.White
                        )
                    }
                }
            }
        )
    }

    LaunchedEffect(creationStatus) {
        if (creationStatus == true) {
            navController.navigate("adminHome") {
                popUpTo("adminHome") { inclusive = true }
            }
        } else if (creationStatus == false) {
            isSubmitting = false
        }
    }

    errorMessage?.let { message ->
        AlertDialog(
            onDismissRequest = { borneViewModel.resetErrorMessage() },
            confirmButton = {
                Button(onClick = { borneViewModel.resetErrorMessage() }) {
                    Text("OK")
                }
            },
            title = { Text("Erreur") },
            text = { Text(message) }
        )
    }
}

