package com.pfe.maborneapp.models

import kotlinx.serialization.Serializable

@Serializable
data class CreateBorneRequest(
    val coord_x: Int?,
    val coord_y: Int?,
    val numero: Int,
    val typeBorne: TypeBorneId,
    val carte: CarteId
)

@Serializable
data class TypeBorneId(val _id: String)

@Serializable
data class CarteId(val _id: String)
