package com.pfe.maborneapp

import com.pfe.maborneapp.utils.FileHandler
import kotlinx.cinterop.*

class IOSPlatform : Platform {
    override val name: String = "iOS"
}

actual fun getPlatform(): Platform = IOSPlatform()



