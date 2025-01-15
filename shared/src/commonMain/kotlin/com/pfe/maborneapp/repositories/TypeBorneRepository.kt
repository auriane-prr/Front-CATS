package com.pfe.maborneapp.repositories

import com.pfe.maborneapp.models.EtatBornes
import com.pfe.maborneapp.models.TypeBorne
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

class TypeBorneRepository(private val client: HttpClient) {
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun fetchTypesBorne(): List<TypeBorne>? {
        println("DEBUG: TypeBorneRepository.fetchTypesBorne()")
        return try {
            val response = client.get("https://back-cats.onrender.com/type_borne")
            println("DEBUG: Réponse brute = ${response.bodyAsText()}")
            if (response.status == HttpStatusCode.OK) {
                val type_borne = json.decodeFromString(ListSerializer(TypeBorne.serializer()), response.bodyAsText())
                println("DEBUG: Bornes disponibles décodées = $type_borne")
                type_borne
            } else {
                println("Erreur lors de la récupération des bornes disponibles : ${response.status}")
                null
            }
        } catch (e: Exception) {
            println("Erreur dans ReservationRepository : ${e.message}")
            null
        }
    }
}
