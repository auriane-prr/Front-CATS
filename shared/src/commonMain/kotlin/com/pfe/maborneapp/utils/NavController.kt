package com.pfe.maborneapp.utils

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.pfe.maborneapp.viewmodel.user.ReservationViewModel

class NavController {
    var currentScreen by mutableStateOf("login")
        private set
    private val argsStack = mutableListOf<Map<String, String>>()

    fun navigate(route: String) {
        currentScreen = route
        println("DEBUG: navigate -> currentScreen=$currentScreen")
    }

    fun extractArgument(key: String): String {
        val args = argsStack.lastOrNull()
        val value = args?.get(key) ?: ""
        println("DEBUG: extractArgument -> key=$key, value=$value, argsStack=$argsStack")
        return value
    }

    fun goBack() {
        if (argsStack.size > 1) {
            argsStack.removeLast()
            currentScreen = argsStack.lastOrNull()?.get("screen") ?: "login"
        }
    }

    fun navigateAndClear(route: String, reservationViewModel: ReservationViewModel) {
        reservationViewModel.clearReservationDetailsAndState()
        navigate(route)
    }

    fun navigateWithParams(route: String, params: Map<String, String> = emptyMap()) {
        argsStack.add(params)
        currentScreen = route
    }

}
