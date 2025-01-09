package com.pfe.maborneapp.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Reservation(
    @SerialName("_id") val id: String,
    val borne: Borne,
    val user: User,
    val dateDebut: String,
    val dateFin: String
)
