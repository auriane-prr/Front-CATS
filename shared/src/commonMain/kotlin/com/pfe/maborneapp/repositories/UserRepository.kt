package com.pfe.maborneapp.repositories

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.json.Json
import com.pfe.maborneapp.models.User

class UserRepository(private val client: HttpClient) {
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun getUserById(userId: String): User? {
        try {
            val response: String = client.get("https://back-cats.onrender.com/users/$userId").body()
            return json.decodeFromString(User.serializer(), response)
        } catch (e: Exception) {
            println("Erreur dans UserRepository : ${e.message}")
        }
        return null
    }
}

