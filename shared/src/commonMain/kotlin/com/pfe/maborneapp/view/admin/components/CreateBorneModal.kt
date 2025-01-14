package com.pfe.maborneapp.view.admin.components

import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pfe.maborneapp.models.CarteId
import com.pfe.maborneapp.models.CreateBorneRequest
import com.pfe.maborneapp.models.TypeBorne
import com.pfe.maborneapp.models.TypeBorneId

@Composable
fun CreateBorneModal(
    typesBorne: List<TypeBorne>,
    onClose: () -> Unit,
    onSubmit: (CreateBorneRequest) -> Unit
) {
    var coordX by remember { mutableStateOf("") }
    var coordY by remember { mutableStateOf("") }
    var numero by remember { mutableStateOf("") }
    var selectedTypeBorne by remember { mutableStateOf<TypeBorne?>(null) }

    AlertDialog(
        onDismissRequest = { onClose() },
        title = { Text("Créer une nouvelle borne") },
        text = {
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                OutlinedTextField(
                    value = coordX,
                    onValueChange = { coordX = it },
                    label = { Text("Coordonnée X (optionnelle)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = coordY,
                    onValueChange = { coordY = it },
                    label = { Text("Coordonnée Y (optionnelle)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = numero,
                    onValueChange = { numero = it },
                    label = { Text("Numéro de la borne") },
                    modifier = Modifier.fillMaxWidth()
                )
                DropdownMenu(
                    expanded = selectedTypeBorne == null,
                    onDismissRequest = { /* fermer si besoin */ }
                ) {
                    typesBorne.forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type.nom) },
                            onClick = { selectedTypeBorne = type }
                        )
                    }
                }
                Text(
                    text = "Type sélectionné : ${selectedTypeBorne?.nom ?: "Aucun"}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                onSubmit(
                    CreateBorneRequest(
                        coord_x = coordX.toIntOrNull(),
                        coord_y = coordY.toIntOrNull(),
                        numero = numero.toInt(),
                        typeBorne = TypeBorneId(selectedTypeBorne?.id ?: ""),
                        carte = CarteId(_id = "6763ed3c4545c40e2a6c7e80") // ID fixe ou dynamique
                    )
                )


            }) {
                Text("Créer")
            }
        },
        dismissButton = { Button(onClick = { onClose() }) { Text("Annuler") } }
    )
}
