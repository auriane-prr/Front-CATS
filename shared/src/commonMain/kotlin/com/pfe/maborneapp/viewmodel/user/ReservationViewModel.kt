package com.pfe.maborneapp.viewmodel.user

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pfe.maborneapp.models.Borne
import com.pfe.maborneapp.models.Carte
import com.pfe.maborneapp.models.CarteId
import com.pfe.maborneapp.models.Reservation
import com.pfe.maborneapp.repositories.ReservationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.pfe.maborneapp.utils.formatDateOnly
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class ReservationViewModel(private val reservationRepository: ReservationRepository) : ViewModel() {
    var selectedDate = mutableStateOf("")
    var startTime = mutableStateOf("")
    var endTime = mutableStateOf("")
    val selectedCarte = mutableStateOf<Carte?>(null)

    private val _reservations = MutableStateFlow<List<Reservation>?>(null)
    val reservations: StateFlow<List<Reservation>?> = _reservations

    private val _availableBornes = MutableStateFlow<List<Borne>?>(null)
    val availableBornes: StateFlow<List<Borne>?> = _availableBornes

    private val _creationStatus = MutableStateFlow<Boolean?>(null)
    val creationStatus: StateFlow<Boolean?> = _creationStatus

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _deleteStatus = MutableStateFlow<Boolean?>(null)
    val deleteStatus: StateFlow<Boolean?> = _deleteStatus

    var lastErrorMessage: String? = null
        private set

    fun fetchReservations(userId: String) {
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

    fun fetchAvailableBornesByCarte(start: String, end: String, carteId: CarteId) {
        viewModelScope.launch {
            try {
                val response = reservationRepository.fetchAvailableBornesByCarte(start, end, carteId)
                println("DEBUG: Bornes disponibles reçues du repository = $response")
                _availableBornes.value = response?.disponible
            } catch (e: Exception) {
                println("DEBUG: Exception dans fetchAvailableBornes : ${e.message}")
                _availableBornes.value = null
            }
        }
    }

    fun createReservation(reservation: Reservation) {
        viewModelScope.launch {
            val errorMessage = reservationRepository.createReservation(reservation)
            if (errorMessage == null) {
                println("DEBUG: Réservation créée avec succès")
                lastErrorMessage = null
                _creationStatus.value = true
            } else {
                println("DEBUG: Erreur lors de la création de la réservation - $errorMessage")
                lastErrorMessage = errorMessage
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

    fun deleteReservation(reservationId: String, userId: String) {
        viewModelScope.launch {
            try {
                reservationRepository.deleteReservation(reservationId)
                _deleteStatus.value = true
                fetchReservations(userId)
            } catch (e: Exception) {
                _deleteStatus.value = false
            }
        }
    }

    fun resetDeleteStatus() {
        _deleteStatus.value = null
    }

}

