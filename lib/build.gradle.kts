import cash.z.ecc.android.Deps
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.dokka") version "1.6.10"
    id("io.kotest.multiplatform")
    id("com.vanniktech.maven.publish") version "0.14.2"
}

group = project.property("GROUP")!!
version = project.property("VERSION_NAME")!!


kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
        withJava()
    }
    js(IR) {
        browser {
            commonWebpackConfig {
                cssSupport.enabled = true
            }
        }
        binaries.executable()
    }
    macosX64()
    macosArm64()
    iosX64()
    iosArm64()
//    linuxX64()

    sourceSets {
        all {
            languageSettings.optIn("kotlin.ExperimentalUnsignedTypes")
            languageSettings.optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
        }
        val commonMain by getting {
            dependencies {
                implementation("io.fluidsonic.locale:fluid-locale:0.11.0")
                implementation(Deps.Kotlin.STDLIB)
                implementation(Deps.SquareOkio.OKIO)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(Deps.Kotest.ASSERTIONS)
                implementation(Deps.Kotest.PROPERTY)
                implementation(Deps.Kotest.FRAMEWORK_ENGINE)
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(Deps.Kotest.RUNNER)
                implementation(Deps.SquareMoshi.MOSHI)
                implementation(Deps.SquareMoshi.MOSHI_KOTLIN)
            }
        }
        val nonJvmMain by creating {
            dependsOn(commonMain)
        }
        val macosX64Main by getting {
            dependsOn(nonJvmMain)
        }
        val macosArm64Main by getting {
            dependsOn(nonJvmMain)
        }
        val jsMain by getting {
            dependsOn(nonJvmMain)
        }
        val iosArm64Main by getting {
            dependsOn(nonJvmMain)
        }
        val iosX64Main by getting {
            dependsOn(nonJvmMain)
        }
    }
}


tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    testLogging {
        showExceptions = true
        showStackTraces = true
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        events = setOf(TestLogEvent.FAILED, TestLogEvent.SKIPPED)
        showStandardStreams = true
    }
}
