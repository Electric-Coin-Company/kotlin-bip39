import cash.z.ecc.android.Deps
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.dokka") version "1.6.10"
    id("io.kotest.multiplatform")
    kotlin("plugin.serialization")
    id("dev.icerock.mobile.multiplatform-resources")
    id("com.vanniktech.maven.publish") version "0.19.0"
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
        browser() // to compile for the web
        nodejs() // to compile against node
    }
    macosX64()
    macosArm64()
    iosX64()
    iosArm64()
    iosSimulatorArm64()
//    linuxX64()

    sourceSets {
        all {
            languageSettings.optIn("kotlin.ExperimentalUnsignedTypes")
        }
        val commonMain by getting {
            dependencies {
                implementation("io.fluidsonic.locale:fluid-locale:0.11.0")
                implementation(Deps.SquareOkio.OKIO)
                implementation("dev.icerock.moko:resources:0.19.0")
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
        val macosX64Main by getting {
            dependsOn(nativeMain)
        }
        val macosArm64Main by getting {
            dependsOn(nativeMain)
        }
        val iosArm64Main by getting {
            dependsOn(nativeMain)
        }
        val iosX64Main by getting {
            dependsOn(nativeMain)
        }
        val iosSimulatorArm64Main by getting {
            dependsOn(nativeMain)
        }
        val macosX64Test by getting {
            dependsOn(nativeTest)
        }
        val macosArm64Test by getting {
            dependsOn(nativeTest)
        }
        val iosArm64Test by getting {
            dependsOn(nativeTest)
        }
        val iosX64Test by getting {
            dependsOn(nativeTest)
        }
        val iosSimulatorArm64Test by getting {
            dependsOn(nativeTest)
        }
        
    }
}

multiplatformResources {
    multiplatformResourcesPackage = "cash.z.ecc.android.bip39"
}




tasks.findByName("macosArm64ProcessResources")!!.dependsOn("generateMRcommonMain")
tasks.findByName("macosArm64ProcessResources")!!.dependsOn("generateMRmacosArm64Main")
tasks.findByName("macosArm64ProcessResources")!!.dependsOn("generateMRiosArm64Main")
tasks.findByName("macosArm64ProcessResources")!!.dependsOn("generateMRiosSimulatorArm64Main")
tasks.findByName("iosArm64SourcesJar")!!.dependsOn("generateMRcommonMain")
tasks.findByName("dokkaHtml")!!.dependsOn("generateMRiosArm64Main")
tasks.findByName("dokkaHtml")!!.dependsOn("generateMRcommonMain")



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


// todo find a better way to fix these warnings
tasks.findByName("macosArm64ProcessResources")!!.dependsOn("generateMRcommonMain")
tasks.findByName("macosArm64ProcessResources")!!.dependsOn("generateMRmacosArm64Main")
tasks.findByName("jvmProcessResources")!!.dependsOn("generateMRjvmMain")

tasks.findByName("jvmSourcesJar")!!.dependsOn("generateMRjvmMain")
tasks.findByName("macosArm64SourcesJar")!!.dependsOn("generateMRmacosArm64Main")
tasks.findByName("macosX64SourcesJar")!!.dependsOn("generateMRmacosX64Main")
tasks.findByName("iosX64SourcesJar")!!.dependsOn("generateMRiosX64Main")
tasks.findByName("iosArm64SourcesJar")!!.dependsOn("generateMRiosArm64Main")

tasks.findByName("dokkaHtml")!!.dependsOn("generateMRiosArm64Main")
tasks.findByName("dokkaHtml")!!.dependsOn("generateMRiosX64Main")
tasks.findByName("dokkaHtml")!!.dependsOn("generateMRcommonMain")