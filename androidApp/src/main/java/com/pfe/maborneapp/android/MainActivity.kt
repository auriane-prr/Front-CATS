package com.pfe.maborneapp.android

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.pfe.maborneapp.App
import com.pfe.maborneapp.utils.appContext

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("DEBUG", "MainActivity: Lancement de l'application")
        appContext = applicationContext
        setContent {
            App(context = applicationContext)
        }
    }
}
