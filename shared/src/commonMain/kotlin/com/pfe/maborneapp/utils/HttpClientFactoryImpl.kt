package com.pfe.maborneapp.utils

import io.ktor.client.HttpClient

expect class HttpClientFactoryImpl() {
    fun create(): HttpClient
}