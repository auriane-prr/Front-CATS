package com.pfe.maborneapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform