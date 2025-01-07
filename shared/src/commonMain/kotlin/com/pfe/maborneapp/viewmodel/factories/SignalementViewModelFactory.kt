package com.pfe.maborneapp.viewmodel.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.pfe.maborneapp.utils.HttpClientFactoryImpl
import com.pfe.maborneapp.repositories.SignalementRepository
import com.pfe.maborneapp.viewmodel.admin.SignalementViewModel
import kotlin.reflect.KClass

class SignalementViewModelFactory : ViewModelProvider.Factory {
    private val client = HttpClientFactoryImpl().create() // Création du client via la factory

    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        if (modelClass == SignalementViewModel::class) {
            return SignalementViewModel(SignalementRepository(client)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}
