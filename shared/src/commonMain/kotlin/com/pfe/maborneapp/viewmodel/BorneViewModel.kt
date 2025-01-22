package com.pfe.maborneapp.viewmodel

import com.pfe.maborneapp.models.*
import com.pfe.maborneapp.repositories.BorneRepository
import com.pfe.maborneapp.utils.provideViewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope

class BorneViewModel(private val borneRepository: BorneRepository,
                     private val viewModelScope: CoroutineScope = provideViewModelScope()
) {
    private val _etatBornes = MutableStateFlow<EtatBornes?>(null)
    val etatBornes: StateFlow<EtatBornes?> = _etatBornes

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _creationStatus = MutableStateFlow<Boolean?>(null)
    val creationStatus: StateFlow<Boolean?> = _creationStatus

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun createBorne(request: CreateBorneRequest) {
        viewModelScope.launch {
            println("DEBUG: Appel à createBorne avec request = $request")
            _creationStatus.value = null // Réinitialise l'état
            try {
                val newBorne = borneRepository.createBorne(request)
                if (newBorne != null) {
                    println("DEBUG: Réservation créée avec succès")
                    _creationStatus.value = true
                    fetchBornesByEtatAndCarte(request.carte)
                } else {
                    println("DEBUG: Échec de la création de la réservation")
                    _creationStatus.value = false
                    _errorMessage.value = "Erreur inconnue lors de la création de la borne."
                }
            } catch (e: Exception) {
                _creationStatus.value = false
                _errorMessage.value = e.message ?: "Erreur inattendue."
            }
        }
    }

    fun deleteBorne(borneId: String) {
        viewModelScope.launch {
            val success = borneRepository.deleteBorne(borneId)
            if (success) {
                fetchBornesByEtat()  // Rafraîchir la liste après suppression
                _errorMessage.value = "Borne supprimée avec succès."
            } else {
                _errorMessage.value = "Échec de la suppression de la borne."
            }
        }
    }

    fun fetchBornesByEtat() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val fetchedEtatBornes = borneRepository.fetchBornesByEtat()
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

    fun fetchBornesByEtatAndCarte(carteId: CarteId) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                println("DEBUG: Appel à fetchBornesByEtatAndCarte avec carteId = $carteId")
                val fetchedEtatBornes = borneRepository.fetchBornesByEtatAndCarte(carteId)
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
