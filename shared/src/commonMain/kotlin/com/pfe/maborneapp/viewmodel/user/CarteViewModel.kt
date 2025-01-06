package com.pfe.maborneapp.viewmodel.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pfe.maborneapp.models.Carte
import com.pfe.maborneapp.repositories.CarteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CarteViewModel(private val repository: CarteRepository) : ViewModel() {
    private val _selectedCarteImageUrl = MutableStateFlow<String?>(null)
    val selectedCarteImageUrl: StateFlow<String?> = _selectedCarteImageUrl

    init {
        selectDefaultCarte()
    }

    private fun selectDefaultCarte() {
        viewModelScope.launch {
            val defaultCarteId = "6763ed3c4545c40e2a6c7e80" // ID de la carte par défaut
            val imageUrl = repository.fetchCarteImageUrl(defaultCarteId)
            _selectedCarteImageUrl.value = imageUrl
            println("DEBUG, selectDefaultCarte - URL de l'image par défaut : $imageUrl")
        }
    }
}
