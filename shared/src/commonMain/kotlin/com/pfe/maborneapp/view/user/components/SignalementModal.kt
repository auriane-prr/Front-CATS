package com.pfe.maborneapp.view.user.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pfe.maborneapp.models.Borne
import com.pfe.maborneapp.view.components.Alert
import com.pfe.maborneapp.viewmodel.SignalementViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignalementModal(
    selectedBorne: Borne?,
    userId: String,
    signalementViewModel: SignalementViewModel,
    onClose: () -> Unit
) {
    if (selectedBorne != null) {
        var reportReason by remember { mutableStateOf(TextFieldValue("")) }
        var showAlert by remember { mutableStateOf(false) }
        var alertMessage by remember { mutableStateOf("") }
        var isSuccess by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { onClose() },
            title = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center // Centrage horizontal
                ) {
                    Text(
                        text = "Signaler la borne ${selectedBorne.numero}",
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }
            },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Divider(
                        color = Color(0xFF045C3C),
                        modifier = Modifier.padding(vertical = 8.dp),
                        thickness = 1.dp
                    )
                    Text(
                        text = "Pourquoi signalez-vous cette borne ?",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                    OutlinedTextField(
                        value = reportReason,
                        onValueChange = { reportReason = it },
                        placeholder = { Text("Expliquez ici...") },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .padding(vertical = 8.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color(0xFF045C3C),
                            unfocusedBorderColor = Color(0xFF045C3C)
                        )
                    )
                }
            },
            confirmButton = {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Button(
                        onClick = { onClose() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                        modifier = Modifier.weight(1f).padding(end = 8.dp)
                    ) {
                        Text(text = "Annuler", color = Color.White)
                    }
                    Button(
                        onClick = {
                            if (reportReason.text.isNotBlank()) {
                                signalementViewModel.signalerBorne(
                                    borneId = selectedBorne.id,
                                    userId = userId,
                                    motif = reportReason.text,
                                    onSuccess = {
                                        isSuccess = true
                                        alertMessage = "Votre signalement a bien été pris en compte."
                                        showAlert = true
                                    },
                                    onError = { errorMessage ->
                                        isSuccess = false
                                        alertMessage = "Erreur : $errorMessage"
                                        showAlert = true
                                    }
                                )
                            } else {
                                isSuccess = false
                                alertMessage = "Veuillez entrer un motif à votre signalement."
                                showAlert = true
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBDD3D0)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Confirmer", color = Color.Black)
                    }
                }
            },
            dismissButton = null
        )

        // Composant d'alerte
        if (showAlert) {
            Alert(
                show = true,
                isSuccess = isSuccess,
                message = alertMessage,
                onDismiss = {
                    showAlert = false
                    if (isSuccess) onClose()
                }
            )
        }
    }
}