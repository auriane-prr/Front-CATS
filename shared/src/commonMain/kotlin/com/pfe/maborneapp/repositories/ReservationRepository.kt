package com.pfe.maborneapp.repositories

import com.pfe.maborneapp.models.Reservation
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ReservationRepository(private val client: HttpClient) {
    private val json = Json { ignoreUnknownKeys = true }

    /*suspend fun createReservation(reservation: Reservation): HttpResponse? {
        return try {
            val response: HttpResponse = client.post("https://back-cats.onrender.com/reservation") {
                contentType(ContentType.Application.Json)
                setBody(
                    json.encodeToString(
                        mapOf(
                            "borne" to mapOf("_id" to reservation.borneId),
                            "user" to mapOf("_id" to reservation.userId),
                            "dateDebut" to reservation.dateDebut,
                            "dateFin" to reservation.dateFin
                        )
                    )
                )
            }
            if (response.status == HttpStatusCode.OK || response.status == HttpStatusCode.Created) {
                response
            } else {
                println("Erreur lors de la création de la réservation : ${response.status}")
                null
            }
        } catch (e: Exception) {
            println("Erreur dans ReservationRepository : ${e.message}")
            null
        }
    }*/

    suspend fun fetchReservationsByUser(userId: String): List<Reservation>? {
        println("DEBUG: Démarrage de la récupération des réservations pour userId = $userId")
        return try {
            val response = client.get("https://back-cats.onrender.com/reservation/user/$userId")
            println("DEBUG: Réponse brute = ${response.bodyAsText()}")
            if (response.status == HttpStatusCode.OK) {
                val reservations = json.decodeFromString(ListSerializer(Reservation.serializer()), response.bodyAsText())
                println("DEBUG: Réservations décodées = $reservations")
                reservations
            } else {
                println("DEBUG: Erreur lors de la récupération des réservations, status = ${response.status}")
                null
            }
        } catch (e: Exception) {
            println("DEBUG: Exception lors de la récupération des réservations : ${e.message}")
            null
        }
    }

}
