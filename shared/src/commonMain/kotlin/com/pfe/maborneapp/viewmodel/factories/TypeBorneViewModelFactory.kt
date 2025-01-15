package com.pfe.maborneapp.viewmodel.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.pfe.maborneapp.repositories.TypeBorneRepository
import com.pfe.maborneapp.utils.HttpClientFactoryImpl
import com.pfe.maborneapp.viewmodel.TypeBorneViewModel
import kotlin.reflect.KClass

class TypeBorneViewModelFactory : ViewModelProvider.Factory {
    private val client = HttpClientFactoryImpl().create()

    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        if (modelClass == TypeBorneViewModel::class) {
            return TypeBorneViewModel(TypeBorneRepository(client)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}
