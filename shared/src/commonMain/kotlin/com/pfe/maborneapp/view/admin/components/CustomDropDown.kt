package com.pfe.maborneapp.view.admin.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pfe.maborneapp.models.TypeBorne
import com.pfe.maborneapp.utils.DarkModeGreen
@Composable
fun CustomDropDown(
    typesBorne: List<TypeBorne>,
    selectedType: TypeBorne?,
    onTypeSelected: (TypeBorne) -> Unit
) {
    var dropdownExpanded by remember { mutableStateOf(false) }
    val darkModeColorTitle = if (isSystemInDarkTheme()) DarkModeGreen else Color(0xFF045C3C)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 2.dp,
                color = darkModeColorTitle,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { dropdownExpanded = !dropdownExpanded }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = selectedType?.nom ?: "Sélectionner un type de borne",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Chevron",
                tint = darkModeColorTitle
            )
        }
    }

    DropdownMenu(
        expanded = dropdownExpanded,
        onDismissRequest = { dropdownExpanded = false },
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        typesBorne.forEach { type ->
            DropdownMenuItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp) // Assure une hauteur uniforme
                    .padding(horizontal = 16.dp),
                text = { Text(type.nom) },
                onClick = {
                    onTypeSelected(type)
                    dropdownExpanded = false
                }
            )
        }
    }
}
