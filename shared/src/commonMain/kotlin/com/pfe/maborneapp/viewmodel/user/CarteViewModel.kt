package com.pfe.maborneapp.viewmodel.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pfe.maborneapp.models.Carte
import com.pfe.maborneapp.repositories.CarteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CarteViewModel(private val repository: CarteRepository) : ViewModel() {
    private val _cartes = MutableStateFlow<List<Carte>>(emptyList())
    val cartes: StateFlow<List<Carte>> = _cartes

    private val _selectedCarteImageUrl = MutableStateFlow<String?>(null)
    val selectedCarteImageUrl: StateFlow<String?> = _selectedCarteImageUrl

    init {
        fetchCartes()
    }

    private fun fetchCartes() {
        viewModelScope.launch {
            val fetchedCartes = repository.fetchCartes()
            if (fetchedCartes != null) {
                println("DEBUG, Cartes récupérées : $fetchedCartes") // Vérifiez si les cartes sont bien récupérées
                _cartes.value = fetchedCartes
            } else {
                println("DEBUG, Aucune carte récupérée") // Log si aucune carte n'est récupérée
            }
        }
    }

    fun selectCarte(carteId: String) {
        viewModelScope.launch {
            println("DEBUG, Carte sélectionnée avec ID : $carteId") // Log de l'ID sélectionné
            val imageUrl = repository.fetchCarteImageUrl(carteId)
            println("DEBUG, URL de l'image générée : $imageUrl") // Log de l'URL générée

            try {
                _selectedCarteImageUrl.value = imageUrl
                println("DEBUG, Image URL mise à jour dans StateFlow")
            } catch (e: Exception) {
                println("DEBUG, Erreur dans selectCarte : ${e.message}")
            }
        }
    }

}


