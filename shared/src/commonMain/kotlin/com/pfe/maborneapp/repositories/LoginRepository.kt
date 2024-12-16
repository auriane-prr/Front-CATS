package com.pfe.maborneapp.repositories

import com.pfe.maborneapp.models.User
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.Json

private val json = Json {
    ignoreUnknownKeys = true
}

class LoginRepository(private val client: HttpClient) {
    suspend fun login(email: String): User? {
        try {
            val requestBody = """{"mail":"$email"}"""
            println("DEBUG - LoginRepository: Corps de la requête = $requestBody")

            val response: HttpResponse = client.post("https://back-cats.onrender.com/auth/login") {
                contentType(ContentType.Application.Json)
                setBody(requestBody)
            }
            println("DEBUG - LoginRepository: Réponse brute = ${response.bodyAsText()}")

            if (response.status == HttpStatusCode.OK) {
                val user = json.decodeFromString<User>(response.bodyAsText()) // Utilise la configuration
                println("DEBUG - LoginRepository: Utilisateur récupéré = $user")
                return user
            } else {
                println("DEBUG - LoginRepository: Code HTTP non OK = ${response.status}")
            }
        } catch (e: Exception) {
            println("DEBUG - LoginRepository: Exception capturée = ${e.message}")
        }
        return null
    }
}
