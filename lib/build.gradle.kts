import cash.z.ecc.android.Deps

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.dokka") version "0.10.1"
    id("com.vanniktech.maven.publish") version "0.14.2"
}

group = project.property("GROUP")!!
version = project.property("VERSION_NAME")!!

//tasks {
//    compileKotlin {
//        kotlinOptions { jvmTarget = 1.8 }
//        sourceCompatibility = 1.8
//    }
//    compileTestKotlin {
//        kotlinOptions { jvmTarget = 1.8 }
//        sourceCompatibility = 1.8
//    }
//
//    // Generate Kotlin/Java documentation from sources.
//    dokka {
//        outputFormat = "html"
//    }
//}

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
        val jvmTest by getting {
            dependencies {
                implementation(Deps.Kotest.RUNNER)
                implementation(Deps.Kotest.ASSERTIONS)
                implementation(Deps.Kotest.PROPERTY)
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


//test {
//    useJUnitPlatform()
//    testLogging {
//        showExceptions = true
//        showStackTraces = true
//        exceptionFormat = "full"
//        events = ["failed", "skipped"]
//        showStandardStreams = true
//    }
//}
