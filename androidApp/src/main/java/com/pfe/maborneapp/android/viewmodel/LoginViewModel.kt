package com.pfe.maborneapp.android.viewmodel

import android.util.Log
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

    fun login(email: String) {
        Log.e("DEBUG", "LoginViewModel: Début de login avec email=$email")
        viewModelScope.launch {
            try {
                val user = repository.login(email)
                if (user != null) {
                    Log.e("DEBUG", "LoginViewModel: Utilisateur récupéré - $user")
                    _userRole.value = if (user.isAdmin()) "Admin" else "User"
                    _loginMessage.value = "Connexion réussie !"
                } else {
                    Log.e("DEBUG", "LoginViewModel: Échec de connexion, utilisateur null")
                    _loginMessage.value = "Email incorrect ou erreur serveur."
                }
            } catch (e: Exception) {
                Log.e("DEBUG", "LoginViewModel: Exception capturée - ${e.message}")
                _loginMessage.value = "Erreur : ${e.message}"
            }
        }
    }
}
