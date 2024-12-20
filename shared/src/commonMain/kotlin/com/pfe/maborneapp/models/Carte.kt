package com.pfe.maborneapp.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Carte(
    @SerialName("_id") val id: String,
    val nom: String,
    val carte: String
)

