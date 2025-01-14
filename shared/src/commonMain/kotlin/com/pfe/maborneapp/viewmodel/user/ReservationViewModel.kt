package com.pfe.maborneapp.viewmodel.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pfe.maborneapp.models.Borne
import com.pfe.maborneapp.models.Reservation
import com.pfe.maborneapp.repositories.ReservationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.pfe.maborneapp.utils.formatDateOnly
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class ReservationViewModel(private val reservationRepository: ReservationRepository) : ViewModel() {
    private val _reservations = MutableStateFlow<List<Reservation>?>(null)
    val reservations: StateFlow<List<Reservation>?> = _reservations

    private val _availableBornes = MutableStateFlow<List<Borne>?>(null)
    val availableBornes: StateFlow<List<Borne>?> = _availableBornes

    private val _creationStatus = MutableStateFlow<Boolean?>(null)
    val creationStatus: StateFlow<Boolean?> = _creationStatus

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun fetchReservations(userId: String) {
        println("DEBUG: Appel à fetchReservations avec userId = $userId")
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val fetchedReservations = reservationRepository.fetchReservationsByUser(userId)
                println("DEBUG: Réservations reçues du repository = $fetchedReservations")
                _reservations.value = fetchedReservations
            } catch (e: Exception) {
                println("DEBUG: Exception dans fetchReservations : ${e.message}")
                _reservations.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchAvailableBornes(start: String, end: String) {
        println("DEBUG: Appel à fetchAvailableBornes avec start = $start et end = $end")
        viewModelScope.launch {
            try {
                val response = reservationRepository.fetchAvailableBornes(start, end)
                println("DEBUG: Bornes disponibles reçues du repository = $response")
                _availableBornes.value = response?.disponible
            } catch (e: Exception) {
                println("DEBUG: Exception dans fetchAvailableBornes : ${e.message}")
                _availableBornes.value = null
            }
        }
    }

    fun createReservation(reservation: Reservation) {
        println("DEBUG: Appel à createReservation")
        viewModelScope.launch {
            try {
                val response = reservationRepository.createReservation(reservation)
                if (response != null) {
                    println("DEBUG: Réservation créée avec succès")
                    _creationStatus.value = true
                } else {
                    println("DEBUG: Échec de la création de la réservation")
                    _creationStatus.value = false
                }
            } catch (e: Exception) {
                println("DEBUG: Exception dans createReservation : ${e.message}")
                _creationStatus.value = false
            }
        }
    }

    fun getSortedReservations(): Pair<List<Reservation>, List<Reservation>> {
        val now = Clock.System.now().toLocalDateTime(TimeZone.UTC)

        val upcomingReservations = _reservations.value?.filter { reservation ->
            val startDate = Instant.parse(reservation.dateDebut).toLocalDateTime(TimeZone.UTC)
            startDate >= now
        }?.sortedBy { reservation ->
            Instant.parse(reservation.dateDebut).toLocalDateTime(TimeZone.UTC)
        } ?: emptyList()

        val pastReservations = _reservations.value?.filter { reservation ->
            val endDate = Instant.parse(reservation.dateFin).toLocalDateTime(TimeZone.UTC)
            endDate < now
        }?.sortedByDescending { reservation ->
            Instant.parse(reservation.dateDebut).toLocalDateTime(TimeZone.UTC)
        } ?: emptyList()

        return Pair(upcomingReservations, pastReservations)
    }

    fun formatReservationDate(dateTime: String): String {
        return formatDateOnly(dateTime)
    }
}

