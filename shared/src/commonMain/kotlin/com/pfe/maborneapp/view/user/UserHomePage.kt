package com.pfe.maborneapp.android.view.user

import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import android.util.Log

@Composable
fun UserHomePage() {
    Log.e("[DEBUG]", "UserHomePage displayed")
    Text("Bienvenue sur la page d'accueil utilisateur !")
}
