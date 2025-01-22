package com.pfe.maborneapp

import androidx.compose.runtime.Composable
import com.pfe.maborneapp.models.CarteId
import com.pfe.maborneapp.utils.NavController
import com.pfe.maborneapp.view.LoginPage
import com.pfe.maborneapp.view.admin.*
import com.pfe.maborneapp.view.user.*
import com.pfe.maborneapp.viewmodel.*
import com.pfe.maborneapp.viewmodel.user.*

@Composable
fun AppNavigation(
    navController: NavController,
    userViewModel: UserViewModel,
    borneViewModel: BorneViewModel,
    signalementViewModel: SignalementViewModel,
    carteViewModel: CarteViewModel,
    reservationViewModel: ReservationViewModel,
    typeBorneViewModel: TypeBorneViewModel
) {
    println("DEBUG: AppNavigation -> currentScreen=${navController.currentScreen}")
    when (navController.currentScreen) {
        "login" -> LoginPage(navController, userViewModel)
        //Pages ADMIN
        "adminHome" -> AdminHomePage(
            navController = navController,
            borneViewModel = borneViewModel,
            carteViewModel = carteViewModel,
            typeBorneViewModel = typeBorneViewModel,
            userViewModel = userViewModel
        )
        "adminSignalement" -> AdminSignalementPage(
            navController = navController,
            carteViewModel = carteViewModel,
            signalementViewModel = signalementViewModel,
            userViewModel = userViewModel
        )
        "adminStatistique" -> AdminStatistiquePage(
            navController,
            userViewModel = userViewModel,
            carteViewModel = carteViewModel)
        "newBorne" -> NewBornePage(
            navController = navController,
            defaultCarteId = navController.extractArgument("carteId"),
            borneViewModel = borneViewModel,
            carteViewModel = carteViewModel,
            typeBorneViewModel = typeBorneViewModel
        )
        //Pages USER
        "userHome" -> UserHomePage(
            navController = navController,
            userViewModel = userViewModel,
            borneViewModel = borneViewModel,
            signalementViewModel = signalementViewModel,
            carteViewModel = carteViewModel
        )
        "reservations" -> ReservationPage(
            navController = navController,
            userViewModel = userViewModel,
            reservationViewModel = reservationViewModel,
        )
        "newReservation" -> NewReservationPage(
            navController = navController,
            reservationViewModel = reservationViewModel,
            carteViewModel = carteViewModel
        )

        "availableBornes" -> AvailableBornesPage(
            navController = navController,
            userViewModel = userViewModel,
            reservationViewModel = reservationViewModel,
            carteViewModel = carteViewModel
        )

        else -> {
            println("DEBUG: AppNavigation -> Écran non reconnu : ${navController.currentScreen}")
            LoginPage(navController, userViewModel)
        }
    }
}
