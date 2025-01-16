package com.pfe.maborneapp.view.admin.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pfe.maborneapp.models.TypeBorne
import com.pfe.maborneapp.utils.DarkContainerColor
import com.pfe.maborneapp.utils.DarkModeGreen
@Composable
fun CustomDropDown(
    typesBorne: List<TypeBorne>,
    selectedType: TypeBorne?,
    onTypeSelected: (TypeBorne) -> Unit
) {
    var dropdownExpanded by remember { mutableStateOf(false) }
    val darkModeColorTitle = if (isSystemInDarkTheme()) DarkModeGreen else Color(0xFF045C3C)
    val darkModeColorBackground = if (isSystemInDarkTheme()) DarkContainerColor else Color.White
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // Champ de sélection
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = darkModeColorBackground,
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
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (dropdownExpanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                    contentDescription = "Chevron",
                    tint = darkModeColorTitle
                )
            }
        }

        // Menu déroulant aligné sous le champ
        DropdownMenu(
            expanded = dropdownExpanded,
            onDismissRequest = { dropdownExpanded = false },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            typesBorne.forEachIndexed { index, type ->
                DropdownMenuItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(50.dp),
                    text = { Text(type.nom, fontSize = 16.sp) },
                    onClick = {
                        onTypeSelected(type)
                        dropdownExpanded = false
                    }
                )
                if (index < typesBorne.size - 1) {
                    Divider(
                        color = darkModeColorTitle.copy(alpha = 0.2f), // Couleur avec transparence
                        thickness = 1.dp,
                        modifier = Modifier.padding(horizontal = 8.dp) // Espacement horizontal
                    )
                }
            }
        }
    }
}

