package com.pfe.maborneapp.viewmodel.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pfe.maborneapp.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _userEmail = MutableStateFlow("")
    val userEmail: StateFlow<String> = _userEmail

    fun fetchUserEmail(userId: String) {
        viewModelScope.launch {
            try {
                val user = userRepository.getUserById(userId)
                if (user != null) {
                    _userEmail.value = user.mail
                } else {
                    println("Erreur : utilisateur non trouvé")
                }
            } catch (e: Exception) {
                println("Erreur dans UserViewModel : ${e.message}")
            }
        }
    }
}
