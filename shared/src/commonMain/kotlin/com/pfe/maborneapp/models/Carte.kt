package com.pfe.maborneapp.models

import kotlinx.serialization.*

@Serializable
data class Carte(
    @SerialName("_id") val id: String,
    val nom: String,
    val carte: String,
    @SerialName("lastModified") val lastModified: String
)