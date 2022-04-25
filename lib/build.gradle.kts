plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("java-library")
    alias(libs.plugins.dokka)
    alias(libs.plugins.publish)
}

group = project.property("GROUP").toString()
version = project.property("VERSION_NAME").toString()

kotlin {
    jvm()
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

