package com.pfe.maborneapp.viewmodel

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

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        fetchSignalements()
    }

    fun fetchSignalements() {
        _isLoading.value = true  // Commence le chargement
        viewModelScope.launch {
            try {
                _signalements.value = repository.getSignalements()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false  // Arrête le chargement, indépendamment du résultat
            }
        }
    }
    

    fun closeSignalement(
        signalementId: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val result = repository.closeSignalement(signalementId)
                if (result) {
                    // Supprime le signalement fermé de la liste
                    _signalements.value = _signalements.value?.filterNot { it.id == signalementId }
                    println("DEBUG: Signalement fermé avec succès.")
                    onSuccess()
                } else {
                    onError("Échec de la fermeture du signalement.")
                }
            } catch (e: Exception) {
                println("Erreur lors de la fermeture du signalement : ${e.message}")
                onError("Erreur : ${e.message}")
            }
        }
    }


    // Fonction pour mettre à jour le statut d'une borne
    fun updateBorneStatus(
        borneId: String,
        newStatus: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val updatedBorne = repository.updateBorneStatus(borneId, newStatus)
                if (updatedBorne != null) {
                    println("Statut de la borne mis à jour avec succès : $updatedBorne")
                    onSuccess()
                } else {
                    onError("Échec de la mise à jour du statut de la borne.")
                }
            } catch (e: Exception) {
                println("Erreur lors de la mise à jour du statut de la borne : ${e.message}")
                onError("Erreur : ${e.message}")
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
