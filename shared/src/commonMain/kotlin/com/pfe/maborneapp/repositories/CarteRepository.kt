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

    suspend fun fetchCartes(): List<Carte>? {
        try {
            val response = client.get("https://back-cats.onrender.com/carte")
            if (response.status == HttpStatusCode.OK) {
                return json.decodeFromString(
                    ListSerializer(Carte.serializer()),
                    response.bodyAsText()
                )
            }
        } catch (e: Exception) {
            println("Erreur dans fetchCartes : ${e.message}")
        }
        return null
    }

    suspend fun fetchCarteImageUrl(carteId: String): String {
        val url = "https://back-cats.onrender.com/carte/photo/$carteId"
        println("DEBUG, fetchCarteImageUrl - URL générée : $url")
        return url
    }

}

