package com.pfe.maborneapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pfe.maborneapp.models.Signalement
import com.pfe.maborneapp.repositories.SignalementRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SignalementViewModel(private val repository: SignalementRepository) : ViewModel() {

    // État des signalements pour l'admin
    private val _signalements = MutableStateFlow<List<Signalement>?>(null)
    val signalements: StateFlow<List<Signalement>?> = _signalements

    // Récupérer les signalements
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

    // Fonction pour signaler une borne (utilisateur)
    fun signalerBorne(
        borneId: String,
        userId: String,
        motif: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        println("DEBUG: signalerBorne appelé avec borneId=$borneId, userId=$userId, motif=$motif")
        viewModelScope.launch {
            try {
                val result = repository.signalerBorne(borneId, userId, motif)
                if (result) {
                    println("DEBUG: Signalement réussi")
                    onSuccess()
                } else {
                    println("DEBUG: Signalement échoué")
                    onError("Signalement échoué")
                }
            } catch (e: Exception) {
                println("Erreur lors de l'envoi du signalement : ${e.message}")
                onError("Erreur : ${e.message}")
            }
        }
    }
}
