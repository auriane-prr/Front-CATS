import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinSerialization)
}


kotlin {
    android {
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
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.6.0")
                implementation(libs.kotlinx.serialization.json) // Ajout de kotlinx-serialization-json
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.androidx.navigation.common)
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
                implementation(libs.kotlinx.serialization.json) // Ajout pour Android
            }
        }
        val androidUnitTest by getting
        val iosMain by creating {
            dependsOn(commonMain)
            dependencies {
                implementation(libs.kotlinx.serialization.json) // Ajout pour iOS
            }
        }
        val iosArm64Main by getting {
            dependsOn(iosMain)
        }
        val iosSimulatorArm64Main by getting {
            dependsOn(iosMain)
        }
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
