package com.pfe.maborneapp

import com.pfe.maborneapp.utils.FileHandler
import io.ktor.http.ContentDisposition.Companion.File
import io.ktor.utils.io.errors.IOException
import java.io.File

class AndroidPlatform : Platform {
    override val name: String = "Android"
}

actual fun getPlatform(): Platform = AndroidPlatform()




