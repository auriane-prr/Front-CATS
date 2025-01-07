package com.pfe.maborneapp.viewmodel.factories.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.pfe.maborneapp.repositories.SignalementRepository
import com.pfe.maborneapp.utils.HttpClientFactoryImpl
import com.pfe.maborneapp.viewmodel.user.SignalementViewModel
import kotlin.reflect.KClass

class SignalementViewModelFactory : ViewModelProvider.Factory {
    private val client = HttpClientFactoryImpl().create()
    private val signalementRepository = SignalementRepository(client)

    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        if (modelClass == SignalementViewModel::class) {
            return SignalementViewModel(signalementRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
