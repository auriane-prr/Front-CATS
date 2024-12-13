package com.pfe.maborneapp

import platform.UIKit.UIDevice

actual fun getPlatform(): Platform {
    return object : Platform {
        override val name: String = "iOS Simulator (Arm64) - " +
                UIDevice.currentDevice.systemName() + " " +
                UIDevice.currentDevice.systemVersion
    }
}
