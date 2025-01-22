import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinSerialization)
    id("org.jetbrains.compose") version "1.5.11"
}

kotlin {
    androidTarget {
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_1_8)
                }
            }
        }
    }

    iosArm64 {
        binaries.framework {
            baseName = "shared"
        }
    }

    iosSimulatorArm64 {
        binaries.framework {
            baseName = "shared"
        }
    }

    sourceSets {
        @OptIn(ExperimentalComposeLibrary::class) val commonMain by getting {
            resources.srcDirs("src/commonMain/resources")
            dependencies {
                // Kotlinx
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.6.0")
                implementation("io.ktor:ktor-client-core:2.3.0")
                implementation("io.ktor:ktor-client-json:2.3.0")
                implementation("io.ktor:ktor-client-content-negotiation:2.3.0")
                implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.0")
                implementation("io.ktor:ktor-client-logging:2.3.0")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")

                // JetBrains Compose
                implementation("org.jetbrains.skiko:skiko:0.7.75")
                implementation("org.jetbrains.compose.foundation:foundation:1.5.0")
                implementation("org.jetbrains.compose.ui:ui-graphics:1.5.0")
                implementation("org.jetbrains.compose.runtime:runtime:1.5.0")
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.materialIconsExtended)
                implementation(compose.components.resources)

                // Koin
                //implementation("io.insert-koin:koin-core:3.5.0")

            }

        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            resources.srcDirs("src/commonMain/resources")
            dependencies {
                implementation("androidx.navigation:navigation-compose:2.8.0-alpha10") // Android-only
                implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.1") // Android-only
                implementation("io.ktor:ktor-client-okhttp:2.3.0") // Android HTTP client
                implementation("androidx.compose.material3:material3:1.3.1")
                implementation("androidx.compose.material3:material3-window-size-class:1.3.1")
                //implementation("io.insert-koin:koin-compose:3.5.0")
            }
        }

        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting

        val iosMain by creating {
            dependsOn(commonMain)
            resources.srcDirs("src/commonMain/resources")
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {
                implementation("io.ktor:ktor-client-darwin:2.3.0") // iOS HTTP client
                implementation("org.jetbrains.skiko:skiko:0.7.75") // Graphismes multiplateformes
            }
        }

    }

    tasks.withType<Copy> {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
}

android {
    namespace = "com.pfe.maborneapp"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    sourceSets["main"].assets.srcDirs("src/commonMain/resources")
}
dependencies {
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.ui.graphics.android)
    implementation(libs.androidx.ui.android)
    implementation(libs.androidx.foundation.android)
    implementation(libs.androidx.core.i18n)
    implementation(libs.androidx.material3.android)
}

tasks.register("assembleXCFramework") {
    dependsOn("linkDebugFrameworkIosArm64", "linkDebugFrameworkIosSimulatorArm64", "build")

    doLast {
        val xcFrameworkDir = buildDir.resolve("xcframework")
        xcFrameworkDir.deleteRecursively()
        xcFrameworkDir.mkdirs()

        val frameworks = listOf(
            buildDir.resolve("bin/iosArm64/debugFramework/shared.framework"),
            buildDir.resolve("bin/iosSimulatorArm64/debugFramework/shared.framework")
        )

        frameworks.forEach {
            if (!it.exists()) {
                throw GradleException("Framework not found at: ${it.absolutePath}")
            }
        }

        val outputDir = xcFrameworkDir.resolve("shared.xcframework")
        exec {
            commandLine(
                "xcodebuild",
                "-create-xcframework",
                *frameworks.flatMap { listOf("-framework", it.absolutePath) }.toTypedArray(),
                "-output",
                outputDir.absolutePath
            )
        }
        println("XCFramework generated at: $outputDir")
    }
}
