import cash.z.ecc.android.Deps
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.dokka") version "1.6.10"
    id("io.kotest.multiplatform")
    kotlin("plugin.serialization")
    id("com.vanniktech.maven.publish") version "0.19.0"
}

group = project.property("GROUP")!!
version = project.property("VERSION_NAME")!!

val enableNative = project.property("NATIVE_TARGETS_ENABLED").toString().toBoolean()
val nativeTargets = if (enableNative) arrayOf(
    "linuxX64",
    "macosX64", "macosArm64",
    "iosArm64", "iosX64", "iosSimulatorArm64",
    "tvosArm64", "tvosX64", "tvosSimulatorArm64",
    "watchosArm32", "watchosArm64", "watchosX86", "watchosX64", "watchosSimulatorArm64",
) else arrayOf()

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
        withJava()
    }
    js(IR) {
        browser() // to compile for the web
        nodejs() // to compile against node
    }
    for (target in nativeTargets) {
        targets.add(presets.getByName(target).createTarget(target))
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(Deps.SquareOkio.OKIO)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(Deps.Kotest.ASSERTIONS)
                implementation(Deps.Kotest.PROPERTY)
                implementation(Deps.Kotest.FRAMEWORK_ENG)
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(Deps.Kotest.RUNNER)
            }
        }
        val nonJvmMain by creating {
            dependsOn(commonMain)
        }
        val jsMain by getting {
            dependsOn(nonJvmMain)
        }
        val nativeMain by creating {
            dependsOn(nonJvmMain)
        }
        val nativeTest by creating {
            dependsOn(commonTest)
        }
        for (target in nativeTargets) {
            getByName("${target}Main").dependsOn(nativeMain)
        }
    }
}

tasks.withType<AbstractTestTask>().configureEach {
    if (this is Test){
        useJUnitPlatform()
    }
    testLogging {
        showExceptions = true
        showStackTraces = true
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        events = setOf(TestLogEvent.FAILED, TestLogEvent.SKIPPED)
        showStandardStreams = true
    }
}

