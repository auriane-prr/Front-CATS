package com.pfe.maborneapp.view.admin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pfe.maborneapp.models.Carte
import com.pfe.maborneapp.models.CarteId
import com.pfe.maborneapp.models.CreateBorneRequest
import com.pfe.maborneapp.models.TypeBorne
import com.pfe.maborneapp.models.TypeBorneId
import com.pfe.maborneapp.utils.*
import com.pfe.maborneapp.view.components.CarteDropdownMenu
import com.pfe.maborneapp.view.admin.components.CustomDropDown
import com.pfe.maborneapp.view.components.image.NetworkImage
import com.pfe.maborneapp.view.components.image.ZoomableImageView
import com.pfe.maborneapp.viewmodel.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewBornePage(
    navController: NavController,
    defaultCarteId: String,
    borneViewModel: BorneViewModel,
    carteViewModel: CarteViewModel,
    typeBorneViewModel: TypeBorneViewModel
) {
    // États pour les champs de la borne et les cartes
    var coordX by remember { mutableStateOf("") }
    var coordY by remember { mutableStateOf("") }
    var numero by remember { mutableStateOf("") }
    val selectedCarte by carteViewModel.selectedCarte.collectAsState()
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
        if (cartes.isNotEmpty() && selectedCarte == null) {
            cartes.find { it.id == defaultCarteId }?.let { carte ->
                carteViewModel.setSelectedCarte(carte)
                carteViewModel.fetchCarteDetails(carte.id)
            }
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
                        IconButton(onClick = { navController.navigate("adminHome") }) {
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
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            color = darkModeColorGreen)
                    } else if (cartes.isNotEmpty()) {
                        CarteDropdownMenu(
                            cartes = cartes,
                            selectedCarte = selectedCarte,
                            onCarteSelected = { carte ->
                                carteViewModel.setSelectedCarte(carte)
                                carteViewModel.fetchCarteDetails(carte.id)
                                typeBorneViewModel.fetchTypesBorne() // Rafraîchir les types de bornes après sélection
                            }
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
                        //lastModified = selectedCarteLastModified,
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
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            color = darkModeColorGreen)
                    } else if (typesBorne.isNotEmpty()) {
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
            navController.navigate("adminHome")
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