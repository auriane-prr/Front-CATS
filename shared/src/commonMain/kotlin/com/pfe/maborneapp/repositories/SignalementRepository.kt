package com.pfe.maborneapp.repositories

import com.pfe.maborneapp.models.Signalement
import kotlinx.serialization.Serializable
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

@Serializable
data class SignalementRequest(
    val borne: Map<String, String>,
    val user: Map<String, String>,
    val motif: String
)

class SignalementRepository(private val httpClient: HttpClient) {

    suspend fun signalerBorne(borneId: String, userId: String, motif: String): Boolean {
        return withContext(Dispatchers.IO) {
            val requestBody = SignalementRequest(
                borne = mapOf("_id" to borneId),
                user = mapOf("_id" to userId),
                motif = motif
            )

            try {
                println("DEBUG: Corps de la requête : $requestBody")
                val response = httpClient.post("https://back-cats.onrender.com/signalement") {
                    contentType(ContentType.Application.Json)
                    setBody(requestBody)
                }

                if (response.status.isSuccess()) {
                    println("DEBUG: Requête réussie avec statut ${response.status}")
                    true
                } else {
                    println("DEBUG: Échec avec statut ${response.status}")
                    false
                }
            } catch (e: Exception) {
                println("Erreur lors de l'envoi du signalement : ${e.message}")
                false
            }
        }
    }

    private val baseUrl = "https://back-cats.onrender.com/signalement"

    suspend fun getSignalements(): List<Signalement> {
        return httpClient.get(baseUrl) {
            contentType(ContentType.Application.Json)
        }.body()
    }
}
