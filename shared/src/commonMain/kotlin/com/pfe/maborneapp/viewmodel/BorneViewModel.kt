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

    init {
        fetchBornesByEtat()
    }

    fun createBorne(request: CreateBorneRequest, onSuccess: (Borne) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val newBorne = repository.createBorne(request)
                if (newBorne != null) {
                    onSuccess(newBorne)
                    fetchBornesByEtat() // Actualiser les bornes après création
                } else {
                    onError("Erreur lors de la création de la borne.")
                }
            } catch (e: Exception) {
                onError("Erreur inattendue : ${e.message}")
            }
        }
    }

    private fun fetchBornesByEtat() {
        viewModelScope.launch {
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
            }
        }
    }

}
