pluginManagement {
    repositories {
        gradlePluginPortal()
    }
    plugins {
        kotlin("jvm") version ("1.6.21") apply(false)
        id("org.jetbrains.dokka") version ("0.10.1") apply (false)
        id("com.vanniktech.maven.publish") version("0.14.2") apply (false)
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
