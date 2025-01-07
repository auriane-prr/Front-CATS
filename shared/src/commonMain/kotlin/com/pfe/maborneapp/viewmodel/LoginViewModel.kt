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

    private val _userId = MutableStateFlow("")
    val userId: StateFlow<String> = _userId // Ajout de l'ID utilisateur

    private val _userRole = MutableStateFlow("")
    val userRole: StateFlow<String> = _userRole

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun login(email: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val user = repository.login(email)
                if (user != null) {
                    _userRole.value = if (user.isAdmin()) "Admin" else "User"
                    _userId.value = user._id // Stocker l'ID utilisateur
                } else {
                    _loginMessage.value = "Email incorrect ou erreur serveur."
                }
            } catch (e: Exception) {
                _loginMessage.value = "Erreur : ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
