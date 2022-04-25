enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    plugins {
        val kotlinVersion = extra["KOTLIN_VERSION"].toString()
        id("org.jetbrains.kotlin.multiplatform") version(kotlinVersion) apply(false)
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "kotlin-bip39"
include(":lib")

includeBuild("build-conventions")
