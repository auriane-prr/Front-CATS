package com.pfe.maborneapp

import androidx.compose.ui.window.ComposeUIViewController
import com.pfe.maborneapp.viewmodel.ViewModelProvider

fun MainViewController() = ComposeUIViewController {
    val userViewModel = ViewModelProvider.provideUserViewMode()
    val borneViewModel = ViewModelProvider.provideBorneViewModel()
    val signalementViewModel = ViewModelProvider.provideSignalementViewModel()
    val carteViewModel = ViewModelProvider.provideCarteViewModel()
    val reservationViewModel = ViewModelProvider.provideReservationViewModel()
    val typeBorneViewModel = ViewModelProvider.provideTypeBorneViewModel()

    App(
        context = null,
        userViewModel = userViewModel,
        borneViewModel = borneViewModel,
        signalementViewModel = signalementViewModel,
        carteViewModel = carteViewModel,
        reservationViewModel = reservationViewModel,
        typeBorneViewModel = typeBorneViewModel
    )
}
