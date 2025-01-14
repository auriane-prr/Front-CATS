package com.pfe.maborneapp.repositories

import com.pfe.maborneapp.models.Borne
import com.pfe.maborneapp.models.CreateBorneRequest
import com.pfe.maborneapp.models.EtatBornes
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import kotlinx.serialization.json.Json

class BorneRepository(private val client: HttpClient) {
    private val json = Json { ignoreUnknownKeys = true }

    // Récupération des bornes par état
    suspend fun fetchBornesByEtat(): EtatBornes? {
        return try {
            val response = client.get("https://back-cats.onrender.com/borne/etat")
            println("DEBUG, Réponse brute : ${response.bodyAsText()}")
            if (response.status == HttpStatusCode.OK) {
                json.decodeFromString(EtatBornes.serializer(), response.bodyAsText())
            } else {
                println("DEBUG, Statut HTTP inattendu : ${response.status}")
                null
            }
        } catch (e: Exception) {
            println("DEBUG, Erreur dans fetchBornesByEtat : ${e.message}")
            null
        }
    }

    // Création d'une nouvelle borne
    suspend fun createBorne(request: CreateBorneRequest): Borne? {
        return try {
            val response = client.post("https://back-cats.onrender.com/borne") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            if (response.status == HttpStatusCode.OK) {
                json.decodeFromString(Borne.serializer(), response.bodyAsText())
            } else {
                println("Erreur : Statut HTTP ${response.status}")
                null
            }
        } catch (e: Exception) {
            println("Erreur lors de la création de la borne : ${e.message}")
            null
        }
    }

    // Mise à jour du statut d'une borne
    suspend fun updateBorneStatus(borneId: String, newStatus: String): Borne? {
        return try {
            val response = client.put("https://back-cats.onrender.com/borne/$borneId/status/$newStatus") {
                contentType(ContentType.Application.Json)
            }
            if (response.status == HttpStatusCode.OK) {
                json.decodeFromString(Borne.serializer(), response.bodyAsText())
            } else {
                println("Erreur : Statut HTTP ${response.status}")
                null
            }
        } catch (e: Exception) {
            println("Erreur lors de la mise à jour du statut de la borne : ${e.message}")
            null
        }
    }
}

