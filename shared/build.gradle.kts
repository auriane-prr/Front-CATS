import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.compose.compiler)
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
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.6.0")
                implementation(libs.kotlinx.serialization.json)
                implementation("io.ktor:ktor-client-core:2.3.0")
                implementation("io.ktor:ktor-client-json:2.3.0")
                implementation("io.ktor:ktor-client-content-negotiation:2.3.0")
                implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.0")
                implementation("io.ktor:ktor-client-logging:2.3.0")

                implementation("org.jetbrains.androidx.navigation:navigation-compose:2.8.0-alpha10")
                implementation("org.jetbrains.androidx.lifecycle:lifecycle-viewmodel-compose:2.8.2")

                implementation("org.jetbrains.skiko:skiko:0.7.75")
                implementation("org.jetbrains.compose.foundation:foundation:1.5.0")
                implementation("org.jetbrains.compose.ui:ui-graphics:1.5.0")


                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.materialIconsExtended)
                implementation(compose.components.resources)
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
                implementation(libs.androidx.navigation.common)
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
                implementation(libs.kotlinx.serialization.json)
                implementation("io.ktor:ktor-client-okhttp:2.3.0")

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
                implementation(libs.kotlinx.serialization.json)
                implementation("io.ktor:ktor-client-darwin:2.3.0")

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
