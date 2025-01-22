package com.pfe.maborneapp.utils

import com.pfe.maborneapp.repositories.*
import io.ktor.client.HttpClient

object DependencyProvider {

    private val httpClient: HttpClient by lazy {
        HttpClientFactoryImpl().create()
    }

    fun provideUserRepository(): UserRepository {
        return UserRepository(client = httpClient)
    }

    fun provideLoginRepository(): LoginRepository {
        return LoginRepository(client = httpClient)
    }

    fun provideBorneRepository(): BorneRepository {
        return BorneRepository(client = httpClient)
    }

    fun provideReservationRepository(): ReservationRepository {
        return ReservationRepository(client = httpClient)
    }

    fun provideCarteRepository(): CarteRepository {
        return CarteRepository(client = httpClient)
    }

    fun provideSignalementRepository(): SignalementRepository {
        return SignalementRepository(httpClient = httpClient)
    }

    fun provideTypeBorneRepository(): TypeBorneRepository {
        return TypeBorneRepository(client = httpClient)
    }
}
