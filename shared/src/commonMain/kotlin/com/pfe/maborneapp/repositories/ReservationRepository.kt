package com.pfe.maborneapp.repositories

import com.pfe.maborneapp.models.CarteId
import com.pfe.maborneapp.models.EtatBornes
import com.pfe.maborneapp.models.IdReference
import com.pfe.maborneapp.models.Reservation
import com.pfe.maborneapp.models.ReservationRequest
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

class ReservationRepository(private val client: HttpClient) {
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun fetchAvailableBornesByCarte(start: String, end: String, carteId: CarteId): EtatBornes? {
        println("DEBUG: Requête fetchAvailableBornes - start = $start, end = $end")
        return try {
            val carteIdString = carteId._id
            val response = client.get("https://back-cats.onrender.com/borne/carte/$carteIdString/etat-date?start=$start&end=$end")
            println("DEBUG: Réponse brute = ${response.bodyAsText()}")
            if (response.status == HttpStatusCode.OK) {
                val bornes = json.decodeFromString(EtatBornes.serializer(), response.bodyAsText())
                println("DEBUG: Bornes disponibles décodées = $bornes")
                bornes
            } else {
                println("Erreur lors de la récupération des bornes disponibles : ${response.status}")
                null
            }
        } catch (e: Exception) {
            println("Erreur dans ReservationRepository : ${e.message}")
            null
        }
    }

    suspend fun createReservation(reservation: Reservation): String? {
        val reservationRequest = ReservationRequest(
            borne = IdReference(reservation.borne.id),
            user = IdReference(reservation.user._id),
            dateDebut = reservation.dateDebut.replace("/", "-"),
            dateFin = reservation.dateFin.replace("/", "-")
        )
        println("DEBUG: Envoi de la réservation : $reservationRequest")

        return try {
            val response: HttpResponse = client.post("https://back-cats.onrender.com/reservation") {
                contentType(ContentType.Application.Json)
                setBody(reservationRequest)
            }
            println("DEBUG: Statut de la réponse : ${response.status}")
            println("DEBUG: Corps de la réponse : ${response.bodyAsText()}")

            if (response.status == HttpStatusCode.OK || response.status == HttpStatusCode.Created) {
                null
            } else {
                val errorBody = response.bodyAsText()
                println("DEBUG: Erreur lors de la création de la réservation - $errorBody")
                errorBody
            }
        } catch (e: Exception) {
            println("DEBUG: Exception lors de la création de la réservation : ${e.message}")
            e.message ?: "Erreur inconnue"
        }
    }

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

    suspend fun deleteReservation(reservationId: String): Boolean {
        return try {
            val response = client.delete("https://back-cats.onrender.com/reservation/$reservationId")
            response.status == HttpStatusCode.OK
        } catch (e: Exception) {
            println("Erreur lors de la suppression de la réservation : ${e.message}")
            false
        }
    }
}
