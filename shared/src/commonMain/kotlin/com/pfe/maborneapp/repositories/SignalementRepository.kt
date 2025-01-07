package com.pfe.maborneapp.repositories

import com.pfe.maborneapp.models.Signalement
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class SignalementRepository(private val httpClient: HttpClient) {
    private val baseUrl = "https://back-cats.onrender.com/signalement"

    suspend fun getSignalements(): List<Signalement> {
        return httpClient.get(baseUrl) {
            contentType(ContentType.Application.Json)
        }.body()
    }
}
