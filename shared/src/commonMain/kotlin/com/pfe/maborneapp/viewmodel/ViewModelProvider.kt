package com.pfe.maborneapp.viewmodel

import com.pfe.maborneapp.utils.DependencyProvider
import com.pfe.maborneapp.viewmodel.user.*

object ViewModelProvider {

    fun provideUserViewMode(): UserViewModel {
        return UserViewModel(
            userRepository = DependencyProvider.provideUserRepository(),
            loginRepository = DependencyProvider.provideLoginRepository()
        )
    }

    fun provideBorneViewModel(): BorneViewModel {
        return BorneViewModel(borneRepository = DependencyProvider.provideBorneRepository())
    }

    fun provideReservationViewModel(): ReservationViewModel {
        return ReservationViewModel(reservationRepository = DependencyProvider.provideReservationRepository())
    }

    fun provideCarteViewModel(): CarteViewModel {
        return CarteViewModel(carteRepository = DependencyProvider.provideCarteRepository())
    }

    fun provideSignalementViewModel(): SignalementViewModel {
        return SignalementViewModel(signalementRepository = DependencyProvider.provideSignalementRepository())
    }

    fun provideTypeBorneViewModel(): TypeBorneViewModel {
        return TypeBorneViewModel(typeBorneRepository = DependencyProvider.provideTypeBorneRepository())
    }
}