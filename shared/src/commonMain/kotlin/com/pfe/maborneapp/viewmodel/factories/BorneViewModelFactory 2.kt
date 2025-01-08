package com.pfe.maborneapp.viewmodel.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.pfe.maborneapp.repositories.BorneRepository
import com.pfe.maborneapp.utils.HttpClientFactoryImpl
import com.pfe.maborneapp.viewmodel.BorneViewModel
import kotlin.reflect.KClass

class BorneViewModelFactory : ViewModelProvider.Factory {
    private val client = HttpClientFactoryImpl().create()

    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        if (modelClass == BorneViewModel::class) {
            return BorneViewModel(BorneRepository(client)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

