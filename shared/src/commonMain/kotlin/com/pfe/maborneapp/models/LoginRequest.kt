package com.pfe.maborneapp.models

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String
)