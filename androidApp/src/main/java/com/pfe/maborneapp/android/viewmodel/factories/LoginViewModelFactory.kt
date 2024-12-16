package com.pfe.maborneapp.android.viewmodel.factories

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pfe.maborneapp.HttpClientFactoryImpl
import com.pfe.maborneapp.android.viewmodel.LoginViewModel
import com.pfe.maborneapp.repositories.LoginRepository

class LoginViewModelFactory : ViewModelProvider.Factory {
    private val client = HttpClientFactoryImpl().create() // Création du client via la factory

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            Log.e("DEBUG", "LoginViewModelFactory: Création de LoginViewModel")
            return LoginViewModel(LoginRepository(client)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
