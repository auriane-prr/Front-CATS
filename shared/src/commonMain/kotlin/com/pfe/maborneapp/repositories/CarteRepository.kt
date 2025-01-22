package com.pfe.maborneapp.repositories

import com.pfe.maborneapp.models.Carte
import com.pfe.maborneapp.models.CarteRequest
import com.pfe.maborneapp.models.TypeBorne
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import io.ktor.http.ContentDisposition.Companion.File
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

class CarteRepository(private val client: HttpClient) {
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun fetchLastModified(carteId: String): String? {
        return try {
            val response = client.get("https://back-cats.onrender.com/carte/$carteId/lastModified")
            if (response.status == HttpStatusCode.OK) {
                response.bodyAsText() // Retourne la date au format ISO 8601
            } else null
        } catch (e: Exception) {
            println("Erreur dans fetchLastModified : ${e.message}")
            null
        }
    }

    suspend fun fetchCarteImageUrl(carteId: String): String {
        val url = "https://back-cats.onrender.com/carte/photo/$carteId"
        println("DEBUG, fetchCarteImageUrl - URL générée : $url")
        return url
    }

    suspend fun fetchCarte(): List<Carte>? {
        println("DEBUG: CarteRepository.fetchCarte()")
        return try {
            val response = client.get("https://back-cats.onrender.com/carte")
            println("DEBUG: CARTE: Réponse brute = ${response.bodyAsText()}")
            if (response.status == HttpStatusCode.OK) {
                val carte = json.decodeFromString(ListSerializer(Carte.serializer()), response.bodyAsText())
                println("DEBUG: Carte disponibles décodées = $carte")
                carte
            } else {
                println("Erreur lors de la récupération des cartes disponibles : ${response.status}")
                null
            }
        } catch (e: Exception) {
            println("Erreur dans CarteRepository : ${e.message}")
            null
        }
    }

    suspend fun fetchCarteById(carteId: String): Carte? {
        val url = "https://back-cats.onrender.com/carte/$carteId"
        println("DEBUG: Appel à l'API pour récupérer la carte avec l'ID $carteId")

        return try {
            val response = client.get(url)
            if (response.status == HttpStatusCode.OK) {
                val carte = response.body<Carte>() // Utilisation de la sérialisation pour décoder la réponse
                println("DEBUG: Carte récupérée = $carte")
                carte
            } else {
                println("Erreur lors de la récupération de la carte : Statut ${response.status}")
                null
            }
        } catch (e: Exception) {
            println("Erreur dans fetchCarteById : ${e.message}")
            null
        }
    }

    suspend fun createCarte(nom: String): Carte? {
        val carteRequest = CarteRequest(nom)
        return try {
            val response = client.post("https://back-cats.onrender.com/carte") {
                contentType(ContentType.Application.Json)
                setBody(carteRequest)
            }
            println("DEBUG: Statut de la réponse : ${response.status}")
            println("DEBUG: Corps de la réponse : ${response.bodyAsText()}")
            if (response.status == HttpStatusCode.Created) {
                json.decodeFromString<Carte>(response.bodyAsText())
            } else {
                println("Erreur lors de la création de la carte: ${response.status}")
                null
            }
        } catch (e: Exception) {
            println("Erreur dans createCarte : ${e.message}")
            null
        }
    }

    suspend fun uploadCartePhoto(carteId: String, photoPath: String): Boolean {
        val fileBytes = fileHandler.readBytes(photoPath)
        if (fileBytes == null) {
            println("Failed to read file bytes.")
            return false
        }

        return try {
            val response = client.patch("https://back-cats.onrender.com/carte/photo/$carteId") {
                setBody(MultiPartFormDataContent(formData {
                    append(
                        key = "photo",
                        value = fileBytes,
                        headers = Headers.build {
                            append(HttpHeaders.ContentDisposition, "form-data; name=\"photo\"; filename=\"${File(photoPath).name}\"")
                            append(HttpHeaders.ContentType, "image/jpeg")
                        }
                    )
                }))
            }
            response.status == HttpStatusCode.OK
        } catch (e: Exception) {
            println("Error in uploadCartePhoto : ${e.message}")
            false
        }
    }

}

