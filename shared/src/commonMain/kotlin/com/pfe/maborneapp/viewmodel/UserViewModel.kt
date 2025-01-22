package com.pfe.maborneapp.viewmodel

import com.pfe.maborneapp.repositories.LoginRepository
import com.pfe.maborneapp.repositories.UserRepository
import com.pfe.maborneapp.utils.provideViewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope

class UserViewModel(
    private val loginRepository: LoginRepository,
    private val userRepository: UserRepository, // Ajout de UserRepository pour la méthode fetchUserEmail
    private val viewModelScope: CoroutineScope = provideViewModelScope()
) {
    // États pour la gestion de la connexion
    private val _userId = MutableStateFlow("")
    val userId: StateFlow<String> = _userId

    private val _userRole = MutableStateFlow("")
    val userRole: StateFlow<String> = _userRole

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // État pour stocker l'email de l'utilisateur
    private val _userEmail = MutableStateFlow("")
    val userEmail: StateFlow<String> = _userEmail

    // Méthode pour la connexion
    fun login(email: String, showAlert: (String, Boolean) -> Unit) {
        println("DEBUG: LoginViewModel -> login() appelé avec l'email : $email")
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val user = loginRepository.login(email)
                if (user != null) {
                    println("DEBUG: Utilisateur trouvé : ${user._id}")
                    _userRole.value = if (user.isAdmin()) "Admin" else "User"
                    _userId.value = user._id
                    _userEmail.value = user.mail // Mise à jour immédiate de l'email
                } else {
                    println("DEBUG: Utilisateur non trouvé")
                    showAlert("Identifiant incorrect", false)
                }
            } catch (e: Exception) {
                println("DEBUG: Erreur dans LoginViewModel : ${e.message}")
                showAlert("Erreur : ${e.message}", false)
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Méthode pour la déconnexion
    fun logout() {
        println("DEBUG: LoginViewModel -> logout() appelé")
        _userId.value = ""
        _userRole.value = ""
        _userEmail.value = ""
    }

    // Méthode pour récupérer l'email utilisateur à partir de l'ID
    fun fetchUserEmail(userId: String) {
        viewModelScope.launch {
            try {
                val user = userRepository.getUserById(userId)
                if (user != null) {
                    println("DEBUG: fetchUserEmail -> Utilisateur trouvé : ${user.mail}")
                    _userEmail.value = user.mail
                } else {
                    println("Erreur : utilisateur non trouvé")
                }
            } catch (e: Exception) {
                println("Erreur dans LoginViewModel : ${e.message}")
            }
        }
    }
}
