package com.pfe.maborneapp.viewmodel.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pfe.maborneapp.repositories.SignalementRepository
import kotlinx.coroutines.launch

class SignalementViewModel(private val repository: SignalementRepository) : ViewModel() {

    fun signalerBorne(borneId: String, userId: String, motif: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
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
