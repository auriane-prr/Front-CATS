package com.pfe.maborneapp.repositories

import com.pfe.maborneapp.models.User
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.Json

class LoginRepository(private val client: HttpClient) {
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun login(email: String): User? {
        try {
            val response: HttpResponse = client.post("https://back-cats.onrender.com/auth/login") {
                contentType(ContentType.Application.Json)
                setBody("""{"mail":"$email"}""")
            }
            if (response.status == HttpStatusCode.OK) {
                return json.decodeFromString(User.serializer(), response.bodyAsText())
            }
        } catch (e: Exception) {
            println("Erreur dans LoginRepository : ${e.message}")
        }
        return null
    }
}
