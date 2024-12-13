package com.pfe.maborneapp.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val mail: String,
    val motDePasse: String,
    val role: String,
    val voitures: List<String>
)
