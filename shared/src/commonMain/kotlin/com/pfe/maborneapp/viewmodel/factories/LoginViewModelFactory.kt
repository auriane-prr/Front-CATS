package com.pfe.maborneapp.viewmodel.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.pfe.maborneapp.HttpClientFactoryImpl
import com.pfe.maborneapp.viewmodel.LoginViewModel
import com.pfe.maborneapp.repositories.LoginRepository
import kotlin.reflect.KClass


class LoginViewModelFactory : ViewModelProvider.Factory {
    private val client = HttpClientFactoryImpl().create() // Création du client via la factory

    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        if (modelClass == LoginViewModel::class) {
            return LoginViewModel(LoginRepository(client)) as T
        }
            throw IllegalArgumentException("Unknown ViewModel")
    }
}
