package com.pfe.maborneapp.viewmodel

import com.pfe.maborneapp.models.TypeBorne
import com.pfe.maborneapp.repositories.TypeBorneRepository
import com.pfe.maborneapp.utils.provideViewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope

class TypeBorneViewModel(private val typeBorneRepository: TypeBorneRepository,private val viewModelScope: CoroutineScope = provideViewModelScope()) {
    private val _typesBorne = MutableStateFlow<List<TypeBorne>>(emptyList())
    val typesBorne: StateFlow<List<TypeBorne>> = _typesBorne

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun fetchTypesBorne() {
        println("DEBUG: Appel à fetchTypesBorne")
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val fetchTypeBorne =typeBorneRepository.fetchTypesBorne()
                if (fetchTypeBorne != null) {
                    println("DEBUG: Types de bornes chargés avec succès : $fetchTypeBorne")
                    _typesBorne.value = fetchTypeBorne
                } else {
                    println("DEBUG: Aucune donnée retournée par le backend.")
                }
            } catch (e: Exception) {
                _errorMessage.value = "Erreur lors de la récupération des types de borne : ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetErrorMessage() {
        _errorMessage.value = null
    }
}
