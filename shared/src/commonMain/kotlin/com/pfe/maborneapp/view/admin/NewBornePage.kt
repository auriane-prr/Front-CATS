package com.pfe.maborneapp.view.admin
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.pfe.maborneapp.models.Borne
import com.pfe.maborneapp.models.CarteId
import com.pfe.maborneapp.models.CreateBorneRequest
import com.pfe.maborneapp.models.TypeBorne
import com.pfe.maborneapp.models.TypeBorneId
import com.pfe.maborneapp.utils.DarkModeGreen
import com.pfe.maborneapp.view.admin.components.CustomDropDown
import com.pfe.maborneapp.view.components.image.NetworkImage
import com.pfe.maborneapp.view.components.image.ZoomableImageView
import com.pfe.maborneapp.viewmodel.BorneViewModel
import com.pfe.maborneapp.viewmodel.CarteViewModel
import com.pfe.maborneapp.viewmodel.factories.BorneViewModelFactory
import com.pfe.maborneapp.viewmodel.factories.CarteViewModelFactory
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewBornePage(navController: NavHostController, typesBorne: List<TypeBorne>, carteId: String? = null) {
    val borneViewModel: BorneViewModel = viewModel(factory = BorneViewModelFactory())
    val carteViewModel: CarteViewModel = viewModel(factory = CarteViewModelFactory())

    var coordX by remember { mutableStateOf("") }
    var coordY by remember { mutableStateOf("") }
    var numero by remember { mutableStateOf("") }
    var selectedTypeBorne by remember { mutableStateOf<TypeBorne?>(null) }
    var dropdownExpanded by remember { mutableStateOf(false) }
    var isSubmitting by remember { mutableStateOf(false) }
    var showZoomableMap by remember { mutableStateOf(false) }

    val creationStatus by borneViewModel.creationStatus.collectAsState()
    val errorMessage by borneViewModel.errorMessage.collectAsState()
    val selectedCarteImageUrl by carteViewModel.selectedCarteImageUrl.collectAsState()
    val selectedCarteLastModified by carteViewModel.selectedCarteLastModified.collectAsState()

    val darkModeColorGreen = if (isSystemInDarkTheme()) DarkModeGreen else Color(0xFF045C3C)

    LaunchedEffect(carteId) {
        carteViewModel.fetchCarteDetails(carteId)
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
                    // Carte interactive
                    NetworkImage(
                        imageUrl = selectedCarteImageUrl,
                        lastModified = selectedCarteLastModified,
                        contentDescription = "Carte Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clickable { showZoomableMap = true }
                            .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                            .padding(8.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Champ Numéro
                    OutlinedTextField(
                        value = numero,
                        onValueChange = { numero = it },
                        label = { Text("Numéro de la borne") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = numero.isEmpty()
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Menu déroulant Type Borne
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White, shape = RoundedCornerShape(8.dp))
                            .border(1.dp, darkModeColorGreen, RoundedCornerShape(8.dp))
                            .clickable { dropdownExpanded = true }
                            .padding(16.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = selectedTypeBorne?.nom ?: "Sélectionner un type de borne",
                                color = Color.Black
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Flèche vers le bas"
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = dropdownExpanded,
                        onDismissRequest = { dropdownExpanded = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        typesBorne.forEach { type ->
                            DropdownMenuItem(
                                text = { Text(type.nom) },
                                onClick = {
                                    selectedTypeBorne = type
                                    dropdownExpanded = false
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Bouton de soumission
                    Button(
                        onClick = {
                            if (numero.isNotEmpty() && selectedTypeBorne != null) {
                                isSubmitting = true
                                borneViewModel.createBorne(
                                    CreateBorneRequest(
                                        coord_x = coordX.toIntOrNull(),
                                        coord_y = coordY.toIntOrNull(),
                                        numero = numero.toInt(),
                                        typeBorne = TypeBorneId(selectedTypeBorne?.id ?: ""),
                                        carte = CarteId(carteId ?: "")
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
