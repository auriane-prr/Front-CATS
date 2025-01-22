package com.pfe.maborneapp.viewmodel

import androidx.compose.runtime.compositionLocalOf
import com.pfe.maborneapp.models.Carte
import com.pfe.maborneapp.repositories.CarteRepository
import com.pfe.maborneapp.utils.provideViewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope

class CarteViewModel(private val carteRepository: CarteRepository,
                     private val viewModelScope: CoroutineScope = provideViewModelScope()
) {

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

    private val defaultCarteId = "6763ed3c4545c40e2a6c7e80"

    private val _selectedCarteId = MutableStateFlow<String?>(null)
    val selectedCarteId: StateFlow<String?> = _selectedCarteId

    fun setSelectedCarteId(carteId: String) {
        _selectedCarteId.value = carteId
        viewModelScope.launch {
            val carte = fetchCarteById(carteId)
            _selectedCarte.value = carte
        }
    }

    fun setSelectedCarte(carte: Carte?) {
        _selectedCarte.value = carte
    }

    fun fetchCarteDetails(carteId: String? = null) {
        viewModelScope.launch {
            try {
                val idToUse = carteId ?: _selectedCarteId.value ?: defaultCarteId
                _selectedCarteLastModified.value = carteRepository.fetchLastModified(idToUse)
                _selectedCarteImageUrl.value = carteRepository.fetchCarteImageUrl(idToUse)
            } catch (e: Exception) {
                _errorMessage.value = "Erreur lors du chargement des détails de la carte : ${e.message}"
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

}

val LocalCarteViewModel = compositionLocalOf<CarteViewModel> {
    error("CarteViewModel not provided")
}

