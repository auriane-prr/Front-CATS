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
        println("DEBUG: LoginRepository -> login() appelé avec l'email : $email")
        try {
            val response: HttpResponse = client.post("https://back-cats.onrender.com/auth/login") {
                contentType(ContentType.Application.Json)
                setBody("""{"mail":"$email"}""")
            }
            println("DEBUG: Réponse du serveur : ${response.status}, body=${response.bodyAsText()}")
            if (response.status == HttpStatusCode.OK) {
                return json.decodeFromString(User.serializer(), response.bodyAsText())
            }
        } catch (e: Exception) {
            println("DEBUG: Erreur dans LoginRepository : ${e.message}")
        }
        return null
    }

}
