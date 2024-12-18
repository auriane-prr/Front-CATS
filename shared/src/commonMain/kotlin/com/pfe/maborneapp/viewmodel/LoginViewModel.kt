package com.pfe.maborneapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pfe.maborneapp.repositories.LoginRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: LoginRepository) : ViewModel() {
    private val _loginMessage = MutableStateFlow("")
    val loginMessage: StateFlow<String> = _loginMessage

    private val _userRole = MutableStateFlow("")
    val userRole: StateFlow<String> = _userRole

    private val _isLoading = MutableStateFlow(false) // Loader ajouté ici
    val isLoading: StateFlow<Boolean> = _isLoading

    fun login(email: String) {
        println("DEBUG, LoginViewModel: Début de login avec email=$email")
        viewModelScope.launch {
            _isLoading.value = true // Activer le loader
            try {
                val user = repository.login(email)
                if (user != null) {
                    println("DEBUG, LoginViewModel: Utilisateur récupéré - $user")
                    _userRole.value = if (user.isAdmin()) "Admin" else "User"
                    _loginMessage.value = "Connexion réussie !"
                } else {
                    println("DEBUG, LoginViewModel: Échec de connexion, utilisateur null")
                    _loginMessage.value = "Email incorrect ou erreur serveur."
                }
            } catch (e: Exception) {
                println("DEBUG, LoginViewModel: Exception capturée - ${e.message}")
                _loginMessage.value = "Erreur : ${e.message}"
            } finally {
                _isLoading.value = false // Désactiver le loader
            }
        }
    }
}