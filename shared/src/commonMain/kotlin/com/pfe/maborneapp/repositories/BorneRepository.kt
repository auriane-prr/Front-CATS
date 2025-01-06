package com.pfe.maborneapp.repositories

import com.pfe.maborneapp.models.Borne
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

class BorneRepository(private val client: HttpClient) {
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun fetchBornes(): List<Borne>? {
        try {
            val response = client.get("https://back-cats.onrender.com/borne")
            if (response.status == HttpStatusCode.OK) {
                return json.decodeFromString(
                    ListSerializer(Borne.serializer()),
                    response.bodyAsText()
                )
            }
        } catch (e: Exception) {
            println("Erreur dans fetchBornes : ${e.message}")
        }
        return null
    }
}
