package com.pfe.maborneapp.models

import kotlinx.serialization.Serializable

@Serializable
data class Signalement(
    val borne: Map<String, String>,
    val user: Map<String, String>,
    val motif: String
)
