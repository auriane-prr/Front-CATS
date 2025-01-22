package com.pfe.maborneapp.android

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.pfe.maborneapp.App
import com.pfe.maborneapp.viewmodel.ViewModelProvider
import com.pfe.maborneapp.utils.appContext

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("DEBUG", "MainActivity: Lancement de l'application")
        appContext = applicationContext

        val userViewModel = ViewModelProvider.provideUserViewMode()
        val borneViewModel = ViewModelProvider.provideBorneViewModel()
        val signalementViewModel = ViewModelProvider.provideSignalementViewModel()
        val carteViewModel = ViewModelProvider.provideCarteViewModel()
        val reservationViewModel = ViewModelProvider.provideReservationViewModel()
        val typeBorneViewModel = ViewModelProvider.provideTypeBorneViewModel()

        setContent {
            App(
                context = applicationContext,
                userViewModel = userViewModel,
                borneViewModel = borneViewModel,
                signalementViewModel = signalementViewModel,
                carteViewModel = carteViewModel,
                reservationViewModel = reservationViewModel,
                typeBorneViewModel = typeBorneViewModel
            )
        }
    }
}
