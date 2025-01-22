package com.pfe.maborneapp

import androidx.compose.runtime.*
import com.pfe.maborneapp.utils.*
import com.pfe.maborneapp.viewmodel.*
import com.pfe.maborneapp.viewmodel.user.*

val localContext = staticCompositionLocalOf<Any?> { null }

@Composable
fun App(
    context: Any? = null,
    userViewModel: UserViewModel,
    borneViewModel: BorneViewModel,
    signalementViewModel: SignalementViewModel,
    carteViewModel: CarteViewModel,
    reservationViewModel: ReservationViewModel,
    typeBorneViewModel: TypeBorneViewModel
) {
    AppTheme {
        val navController = remember { NavController() }
        CompositionLocalProvider(localContext provides context) {
            AppNavigation(
                navController = navController,
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