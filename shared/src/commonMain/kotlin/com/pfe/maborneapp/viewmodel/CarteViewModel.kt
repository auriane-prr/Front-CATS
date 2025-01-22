package com.pfe.maborneapp.viewmodel

import androidx.compose.runtime.compositionLocalOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pfe.maborneapp.models.Carte
import com.pfe.maborneapp.models.TypeBorne
import com.pfe.maborneapp.repositories.CarteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class CarteViewModel(private val carteRepository: CarteRepository) : ViewModel() {

    private val _carte = MutableStateFlow<List<Carte>>(emptyList())
    val carte: StateFlow<List<Carte>> = _carte

    private val _selectedCarte = MutableStateFlow<Carte?>(null)
    val selectedCarte: StateFlow<Carte?> = _selectedCarte

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _selectedCarteImageUrl = MutableStateFlow<String?>(null)
    val selectedCarteImageUrl: StateFlow<String?> = _selectedCarteImageUrl

    private val _selectedCarteLastModified = MutableStateFlow<String?>(null)
    val selectedCarteLastModified: StateFlow<String?> = _selectedCarteLastModified

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val defaultCarteId = "6763ed3c4545c40e2a6c7e80" // ID de la carte par défaut

    fun setSelectedCarte(carte: Carte?) {
        _selectedCarte.value = carte
    }

    fun fetchCarteDetails(carteId: String? = null) {
        viewModelScope.launch {
            try {
                val idToUse = carteId ?: defaultCarteId

                // Récupérer la date de dernière modification
                val lastModified = carteRepository.fetchLastModified(idToUse)
                _selectedCarteLastModified.value = lastModified

                // Récupérer l'URL de l'image
                val imageUrl = carteRepository.fetchCarteImageUrl(idToUse)
                _selectedCarteImageUrl.value = imageUrl

                println("DEBUG: fetchCarteDetails - Chargé : $idToUse avec URL $imageUrl et lastModified $lastModified")
            } catch (e: Exception) {
                println("DEBUG:Erreur dans fetchCarteDetails : ${e.message}")
            }
        }
    }

    fun fetchCartes() {
        println("DEBUG: Appel à fetchCartes()")
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val fetchCarte = carteRepository.fetchCarte()
                if (fetchCarte != null) {
                    println("DEBUG: Carte chargés avec succès : $fetchCarte")
                    _carte.value = fetchCarte
                } else {
                    println("DEBUG: Aucune donnée retournée par le backend.")
                }
            } catch (e: Exception) {
                _errorMessage.value = "Erreur lors de la récupération des cartes : ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    suspend fun fetchCarteById(carteId: String): Carte? {
        println("DEBUG: fetchCarteById appelé avec carteId = $carteId")

        return try {
            val fetchedCarte = carteRepository.fetchCarteById(carteId)
            println("DEBUG: Carte récupérée via API : ${fetchedCarte?.nom}")
            fetchedCarte
        } catch (e: Exception) {
            println("Erreur lors de la récupération de la carte : ${e.message}")
            null
        }
    }

    fun createCarte(nom: String, onComplete: (Carte?) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            val newCarte = carteRepository.createCarte(nom)
            if (newCarte != null) {
                _carte.value = _carte.value + newCarte
                onComplete(newCarte)
            } else {
                _errorMessage.value = "Erreur lors de la création de la carte"
                onComplete(null)
            }
            _isLoading.value = false
        }
    }

    fun uploadCartePhoto(carte: Carte, photoPath: String, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = carteRepository.uploadCartePhoto(carte.id, photoPath)
            if (result) {
                _selectedCarteImageUrl.value = "https://back-cats.onrender.com/carte/photo/${carte.id}" // Mise à jour de l'URL
                onComplete(true)
            } else {
                _errorMessage.value = "Erreur lors de l'upload de la photo"
                onComplete(false)
            }
            _isLoading.value = false
        }
    }


}

val LocalCarteViewModel = compositionLocalOf<CarteViewModel> {
    error("CarteViewModel not provided")
}



