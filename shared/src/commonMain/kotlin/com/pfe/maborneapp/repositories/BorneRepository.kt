package com.pfe.maborneapp.repositories

import com.pfe.maborneapp.models.Borne
import com.pfe.maborneapp.models.EtatBornes
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import kotlinx.serialization.json.Json

class BorneRepository(private val client: HttpClient) {
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun fetchBornesByEtat(): EtatBornes? {
        try {
            val response = client.get("https://back-cats.onrender.com/borne/etat")
            println("DEBUG, Réponse brute : ${response.bodyAsText()}")
            if (response.status == HttpStatusCode.OK) {
                return json.decodeFromString(EtatBornes.serializer(), response.bodyAsText())
            } else {
                println("DEBUG, Statut HTTP inattendu : ${response.status}")
            }
        } catch (e: Exception) {
            println("DEBUG, Erreur dans fetchBornesByEtat : ${e.message}")
        }
        return null
    }

}
