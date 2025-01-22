package com.pfe.maborneapp.models

import kotlinx.serialization.*

@Serializable
data class Reservation(
    @SerialName("_id") val id: String,
    val borne: Borne,
    val user: User,
    val dateDebut: String,
    val dateFin: String
)

@Serializable
data class ReservationRequest(
    val borne: IdReference,
    val user: IdReference,
    val dateDebut: String,
    val dateFin: String
)

@Serializable
data class IdReference(
    @SerialName("_id") val id: String
)