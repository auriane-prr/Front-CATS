package com.pfe.maborneapp.viewmodel.factories.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.pfe.maborneapp.repositories.ReservationRepository
import com.pfe.maborneapp.utils.HttpClientFactoryImpl
import com.pfe.maborneapp.viewmodel.user.ReservationViewModel
import kotlin.reflect.KClass

class ReservationViewModelFactory : ViewModelProvider.Factory {
    private val client = HttpClientFactoryImpl().create()
    private val reservationRepository = ReservationRepository(client)

    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        if (modelClass == ReservationViewModel::class) {
            return ReservationViewModel(reservationRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}