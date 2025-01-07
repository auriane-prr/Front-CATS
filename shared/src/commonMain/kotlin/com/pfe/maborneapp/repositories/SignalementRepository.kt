package com.pfe.maborneapp.repositories

import com.pfe.maborneapp.models.Signalement
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class SignalementRepository(private val httpClient: HttpClient) {
    suspend fun signalerBorne(borneId: String, userId: String, motif: String): Boolean {
        return withContext(Dispatchers.IO) {
            val requestBody = Signalement(
                borne = mapOf("_id" to borneId),
                user = mapOf("_id" to userId),
                motif = motif
            )

            try {
                httpClient.post("https://back-cats.onrender.com/signalement") {
                    contentType(ContentType.Application.Json)
                    setBody(requestBody)
                }
                println("DEBUG: Requête réussie")
                true // Si la requête réussit
            } catch (e: Exception) {
                println("Erreur lors de l'envoi du signalement : ${e.message}")
                false // Si la requête échoue
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