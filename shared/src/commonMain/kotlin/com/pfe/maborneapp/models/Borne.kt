package com.pfe.maborneapp.models

import kotlinx.serialization.*

@Serializable
data class Borne(
    @SerialName("_id") val id: String,
    val status: String,
    val coord_x: Int?,
    val coord_y: Int?,
    val numero: Int,
    val typeBorne: TypeBorne,
    val carte: CarteInfo
)

@Serializable
data class TypeBorne(
    @SerialName("_id") val id: String,
    val nom: String
)

@Serializable
data class CarteInfo(
    @SerialName("_id") val id: String,
    val nom: String,
    val carte: String
)

@Serializable
data class EtatBornes(
    val disponible: List<Borne>,
    val occupee: List<Borne>,
    val hs: List<Borne>,
    val signalee: List<Borne>
)
