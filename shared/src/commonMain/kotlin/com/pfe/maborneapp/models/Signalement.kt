package com.pfe.maborneapp.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Signalement(
    @SerialName("_id") val id: String? = null,
    val borne: Borne,
    val user: User,
    val etat: String,
    val motif: String
)
