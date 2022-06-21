plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("java-library")
    alias(libs.plugins.dokka)
    alias(libs.plugins.publish)
    id("bip39.kotlin-multiplatform-conventions")
    id("bip39.dependency-conventions")
}

project.group = project.property("GROUP").toString()

val libraryVersion = project.property("VERSION_NAME").toString()
project.version = if (project.property("IS_SNAPSHOT").toString().toBoolean()) {
    "$version-SNAPSHOT"
} else {
    version
}

kotlin {
    jvm {
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting {
            dependencies {
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.kotest.runner)
                implementation(libs.kotest.assertion)
                implementation(libs.kotest.property)
                implementation(libs.moshi.core)
                implementation(libs.moshi.kotlin)
            }
        }
    }
}