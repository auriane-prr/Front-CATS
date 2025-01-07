package com.pfe.maborneapp.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Signalement(
    @SerialName("_id") val id: String? = null,
    val borne: Map<String, String>,
    val user: Map<String, String>,
    val motif: String
)
