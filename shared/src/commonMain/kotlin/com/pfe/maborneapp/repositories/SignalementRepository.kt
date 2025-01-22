package com.pfe.maborneapp.repositories

import com.pfe.maborneapp.models.*
import kotlinx.serialization.Serializable
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

@Serializable
data class SignalementRequest(
    val borne: Map<String, String>,
    val user: Map<String, String>,
    val motif: String
)

class SignalementRepository(private val httpClient: HttpClient) {
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun updateBorneStatus(borneId: String, newStatus: String): Boolean {
        return withContext(Dispatchers.Default) {
            try {
                val response = httpClient.put("https://back-cats.onrender.com/borne/$borneId/status/$newStatus") {
                    contentType(ContentType.Application.Json)
                }

                if (response.status.isSuccess()) {
                    println("DEBUG: UPDATE_BORNE: Statut de la borne mis à jour avec succès : ${response.status}")
                    true
                } else {
                    println("DEBUG: Échec de la mise à jour du statut : ${response.status}")
                    false
                }
            } catch (e: Exception) {
                println("DEBUG: Erreur lors de la mise à jour du statut : ${e.message}")
                false
            }
        }
    }

    suspend fun closeSignalement(signalementId: String): Boolean {
        return try {
            val response = httpClient.put("https://back-cats.onrender.com/signalement/$signalementId/close") {
                contentType(ContentType.Application.Json)
            }
            if (response.status.isSuccess()) {
                println("DEBUG: Signalement fermé avec succès pour ID : $signalementId")
                true
            } else {
                println("DEBUG: Erreur : Statut HTTP ${response.status}")
                false
            }
        } catch (e: Exception) {
            println("DEBUG: Erreur lors de la fermeture du signalement : ${e.message}")
            false
        }
    }


    suspend fun signalerBorne(borneId: String, userId: String, motif: String): Boolean {
        return withContext(Dispatchers.Default) {
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
                println("DEBUG: Erreur lors de l'envoi du signalement : ${e.message}")
                false
            }
        }
    }


    suspend fun getSignalements(): List<Signalement>? {
        return try {
            val response = httpClient.get("https://back-cats.onrender.com/signalement/en-attente")
            println("DEBUG, Réponse brute : ${response.bodyAsText()}")
            if (response.status == HttpStatusCode.OK) {
                val signalements = json.decodeFromString(ListSerializer(Signalement.serializer()), response.bodyAsText())
                println("DEBUG, Signalements récupérés avec succès= $signalements")
                signalements
            } else {
                println("DEBUG, Statut HTTP inattendu : ${response.status}")
                null
            }
        } catch (e: Exception) {
            println("DEBUG, Erreur dans fetchBornesByEtat : ${e.message}")
            null
        }
    }

    suspend fun getSignalementsByCarte(carteId: CarteId): List<Signalement>? {
        return try {
            val carteIdString = carteId._id
            val response = httpClient.get("https://back-cats.onrender.com/signalement/carte/$carteIdString/en-attente")
            println("DEBUG, Signalement:  Réponse brute : ${response.bodyAsText()}")
            if (response.status == HttpStatusCode.OK) {
                val signalements = json.decodeFromString(ListSerializer(Signalement.serializer()), response.bodyAsText())
                println("DEBUG, Signalements récupérés avec succès= $signalements")
                signalements
            } else {
                println("DEBUG, Statut HTTP inattendu : ${response.status}")
                null
            }
        } catch (e: Exception) {
            println("DEBUG, Erreur dans fetchBornesByEtat : ${e.message}")
            null
        }
    }
}
