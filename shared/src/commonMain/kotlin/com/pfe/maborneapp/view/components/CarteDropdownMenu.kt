package com.pfe.maborneapp.view.components
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pfe.maborneapp.models.Carte
import com.pfe.maborneapp.utils.DarkContainerColor
import com.pfe.maborneapp.utils.DarkModeGreen

@Composable
fun CarteDropdownMenu(
    cartes: List<Carte>,
    selectedCarte: Carte?,
    onCarteSelected: (Carte) -> Unit
) {
    var dropdownExpanded by remember { mutableStateOf(false) }
    val darkModeColorTitle = if (isSystemInDarkTheme()) DarkModeGreen else Color(0xFF045C3C)
    val darkModeColorBackground = if (isSystemInDarkTheme()) DarkContainerColor else Color.White

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
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
                    text = selectedCarte?.nom ?: "Sélectionner une carte",
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

        DropdownMenu(
            expanded = dropdownExpanded,
            onDismissRequest = { dropdownExpanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            cartes.forEach { carte ->
                DropdownMenuItem(
                    text = { Text(carte.nom, fontSize = 16.sp) },
                    onClick = {
                        onCarteSelected(carte)
                        dropdownExpanded = false
                    }
                )
            }
        }
    }
}
