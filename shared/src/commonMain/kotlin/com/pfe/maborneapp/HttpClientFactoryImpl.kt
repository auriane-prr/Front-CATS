package com.pfe.maborneapp

import io.ktor.client.*

expect class HttpClientFactoryImpl() {
    fun create(): HttpClient
}

