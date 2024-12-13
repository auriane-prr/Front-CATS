package com.pfe.maborneapp.repositories

class LoginRepository {
    fun authenticate(email: String, password: String): String {
        return if (email == "test@example.com" && password == "password") {
            "Connexion réussie !"
        } else {
            "Email ou mot de passe incorrect."
        }
    }
}
