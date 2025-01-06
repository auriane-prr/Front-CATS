package com.pfe.maborneapp.viewmodel.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.pfe.maborneapp.utils.HttpClientFactoryImpl
import com.pfe.maborneapp.repositories.CarteRepository
import com.pfe.maborneapp.viewmodel.CarteViewModel
import kotlin.reflect.KClass

class CarteViewModelFactory : ViewModelProvider.Factory {
    private val client = HttpClientFactoryImpl().create() // Création du client via la factory

    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        if (modelClass == CarteViewModel::class) {
            return CarteViewModel(CarteRepository(client)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}