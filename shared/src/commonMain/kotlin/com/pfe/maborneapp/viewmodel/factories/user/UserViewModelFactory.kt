package com.pfe.maborneapp.viewmodel.factories.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.pfe.maborneapp.repositories.UserRepository
import com.pfe.maborneapp.utils.HttpClientFactoryImpl
import com.pfe.maborneapp.viewmodel.user.UserViewModel
import kotlin.reflect.KClass

class UserViewModelFactory : ViewModelProvider.Factory {
    private val client = HttpClientFactoryImpl().create()
    private val userRepository = UserRepository(client)

    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        if (modelClass == UserViewModel::class) {
            return UserViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}
