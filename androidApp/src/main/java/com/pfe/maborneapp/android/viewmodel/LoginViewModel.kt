package com.pfe.maborneapp.android.viewmodel

import androidx.lifecycle.ViewModel
import com.pfe.maborneapp.repositories.LoginRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LoginViewModel(private val repository: LoginRepository) : ViewModel() {
    private val _loginMessage = MutableStateFlow("")
    val loginMessage: StateFlow<String> = _loginMessage

    private val _userRole = MutableStateFlow("")
    val userRole: StateFlow<String> = _userRole

    fun login(email: String, password: String) {
        _loginMessage.value = repository.authenticate(email, password)
        // Exemple simple pour déterminer le rôle
        _userRole.value = if (email.contains("admin")) "admin" else "user"
    }
}
