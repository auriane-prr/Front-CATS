package com.pfe.maborneapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pfe.maborneapp.models.Borne
import com.pfe.maborneapp.repositories.BorneRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BorneViewModel(private val repository: BorneRepository) : ViewModel() {
    private val _bornes = MutableStateFlow<List<Borne>?>(null)
    val bornes: StateFlow<List<Borne>?> = _bornes

    init {
        fetchBornes()
    }

    private fun fetchBornes() {
        viewModelScope.launch {
            val fetchedBornes = repository.fetchBornes()
            _bornes.value = fetchedBornes
        }
    }
}
