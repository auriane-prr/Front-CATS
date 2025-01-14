package com.pfe.maborneapp.repositories

import com.pfe.maborneapp.models.Carte
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

class CarteRepository(private val client: HttpClient) {
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun fetchLastModified(carteId: String): String? {
        return try {
            val response = client.get("https://back-cats.onrender.com/carte/$carteId/lastModified")
            if (response.status == HttpStatusCode.OK) {
                response.bodyAsText() // Retourne la date au format ISO 8601
            } else null
        } catch (e: Exception) {
            println("Erreur dans fetchLastModified : ${e.message}")
            null
        }
    }

    suspend fun fetchCarteImageUrl(carteId: String): String {
        val url = "https://back-cats.onrender.com/carte/photo/$carteId"
        println("DEBUG, fetchCarteImageUrl - URL générée : $url")
        return url
    }

}

