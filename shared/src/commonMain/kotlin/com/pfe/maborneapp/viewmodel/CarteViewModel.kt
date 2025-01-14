package com.pfe.maborneapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pfe.maborneapp.repositories.CarteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CarteViewModel(private val carteRepository: CarteRepository) : ViewModel() {

    private val _selectedCarteImageUrl = MutableStateFlow<String?>(null)
    val selectedCarteImageUrl: StateFlow<String?> = _selectedCarteImageUrl

    private val _selectedCarteLastModified = MutableStateFlow<String?>(null)
    val selectedCarteLastModified: StateFlow<String?> = _selectedCarteLastModified

    private val defaultCarteId = "6763ed3c4545c40e2a6c7e80" // ID de la carte par défaut

    fun fetchCarteDetails(carteId: String? = null) {
        viewModelScope.launch {
            try {
                val idToUse = carteId ?: defaultCarteId // Utilise le `carteId` dynamique ou par défaut

                // Récupérer la date de dernière modification
                val lastModified = carteRepository.fetchLastModified(idToUse)
                _selectedCarteLastModified.value = lastModified

                // Récupérer l'URL de l'image
                val imageUrl = carteRepository.fetchCarteImageUrl(idToUse)
                _selectedCarteImageUrl.value = imageUrl

                println("DEBUG, fetchCarteDetails - Chargé : $idToUse avec URL $imageUrl et lastModified $lastModified")
            } catch (e: Exception) {
                println("Erreur dans fetchCarteDetails : ${e.message}")
            }
        }
    }
}
