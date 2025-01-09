package com.pfe.maborneapp.viewmodel.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pfe.maborneapp.models.Reservation
import com.pfe.maborneapp.repositories.ReservationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ReservationViewModel(private val reservationRepository: ReservationRepository) : ViewModel() {
    private val _reservations = MutableStateFlow<List<Reservation>?>(null)
    val reservations: StateFlow<List<Reservation>?> = _reservations

    fun fetchReservations(userId: String) {
        println("DEBUG: Appel à fetchReservations avec userId = $userId")
        viewModelScope.launch {
            try {
                val fetchedReservations = reservationRepository.fetchReservationsByUser(userId)
                println("DEBUG: Réservations reçues du repository = $fetchedReservations")
                _reservations.value = fetchedReservations
            } catch (e: Exception) {
                println("DEBUG: Exception dans fetchReservations : ${e.message}")
                _reservations.value = null
            }
        }
    }

}
