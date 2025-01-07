package com.pfe.maborneapp.viewmodel.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pfe.maborneapp.models.Signalement
import com.pfe.maborneapp.repositories.SignalementRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SignalementViewModel(private val repository: SignalementRepository) : ViewModel() {

    private val _signalements = MutableStateFlow<List<Signalement>?>(null)
    val signalements: StateFlow<List<Signalement>?> = _signalements

    init {
        fetchSignalements()
    }

    private fun fetchSignalements() {
        viewModelScope.launch {
            try {
                _signalements.value = repository.getSignalements()
            } catch (e: Exception) {
                e.printStackTrace()
                _signalements.value = emptyList()
            }
        }
    }
}
