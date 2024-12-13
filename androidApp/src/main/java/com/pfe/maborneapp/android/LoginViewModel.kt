package com.pfe.maborneapp.android

import androidx.lifecycle.ViewModel
import com.pfe.maborneapp.LoginRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LoginViewModel(private val repository: LoginRepository) : ViewModel() {
    private val _loginMessage = MutableStateFlow("")
    val loginMessage: StateFlow<String> = _loginMessage

    fun login(email: String, password: String) {
        _loginMessage.value = repository.authenticate(email, password)
    }
}

