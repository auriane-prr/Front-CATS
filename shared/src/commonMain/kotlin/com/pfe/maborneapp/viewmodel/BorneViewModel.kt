package com.pfe.maborneapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pfe.maborneapp.models.Borne
import com.pfe.maborneapp.models.CreateBorneRequest
import com.pfe.maborneapp.models.EtatBornes
import com.pfe.maborneapp.repositories.BorneRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BorneViewModel(private val repository: BorneRepository) : ViewModel() {
    private val _etatBornes = MutableStateFlow<EtatBornes?>(null)
    val etatBornes: StateFlow<EtatBornes?> = _etatBornes

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _creationStatus = MutableStateFlow<Boolean?>(null)
    val creationStatus: StateFlow<Boolean?> = _creationStatus

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        fetchBornesByEtat()
    }

    fun createBorne(request: CreateBorneRequest) {
        viewModelScope.launch {
            _creationStatus.value = null // Réinitialise l'état
            try {
                val newBorne = repository.createBorne(request)
                if (newBorne != null) {
                    _creationStatus.value = true
                    fetchBornesByEtat() // Actualiser les bornes après création
                } else {
                    _creationStatus.value = false
                    _errorMessage.value = "Erreur inconnue lors de la création de la borne."
                }
            } catch (e: Exception) {
                _creationStatus.value = false
                _errorMessage.value = e.message ?: "Erreur inattendue."
            }
        }
    }


    fun fetchBornesByEtat() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val fetchedEtatBornes = repository.fetchBornesByEtat()
                if (fetchedEtatBornes != null) {
                    println("DEBUG, Bornes chargées avec succès : $fetchedEtatBornes")
                    _etatBornes.value = fetchedEtatBornes
                } else {
                    println("DEBUG, Aucune donnée retournée par le backend.")
                }
            } catch (e: Exception) {
                println("DEBUG, Erreur lors du chargement des bornes : ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun resetErrorMessage() {
        _errorMessage.value = null
    }

}
