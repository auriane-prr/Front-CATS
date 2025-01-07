package com.pfe.maborneapp.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Signalement(
    @SerialName("_id") val id: String,
    val motif: String,
    val date: String,
    val user: User,
    val borne: Borne
)


