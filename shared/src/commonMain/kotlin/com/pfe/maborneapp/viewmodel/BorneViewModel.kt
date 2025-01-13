package com.pfe.maborneapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pfe.maborneapp.models.Borne
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

    init {
        fetchBornesByEtat()
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

}
