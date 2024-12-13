enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev") // Dépôt Compose JetBrains
        maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev") // Dépôt Kotlin Dev
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev") // Dépôt Compose JetBrains
        maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev") // Dépôt Kotlin Dev
    }
}

rootProject.name = "MaBorneApp"
include(":androidApp")
include(":shared")
// Ajouter le projet iosApp
include(":iosApp")

// Spécifiez le chemin vers le projet Xcode
project(":iosApp").projectDir = file("../Bureau/iosApp")