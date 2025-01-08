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

    init {
        fetchBornesByEtat()
    }

    private fun fetchBornesByEtat() {
        viewModelScope.launch {
            val fetchedEtatBornes = repository.fetchBornesByEtat()
            _etatBornes.value = fetchedEtatBornes
        }
    }
}
