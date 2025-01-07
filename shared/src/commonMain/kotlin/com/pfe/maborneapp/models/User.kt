package com.pfe.maborneapp.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val _id: String,
    val mail: String,
    val role: String,
    val voitures: List<String>? = null,
    val motDePasse: String? = null
) {

    fun isAdmin(): Boolean {
        return role == "Admin"
    }
}
