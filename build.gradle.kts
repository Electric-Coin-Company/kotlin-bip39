
buildscript {
    repositories {
        mavenLocal()
        google()
        mavenCentral()
    }

}

plugins {
    kotlin("multiplatform").version(cash.z.ecc.android.Deps.kotlinVersion) apply false
    id("io.kotest.multiplatform") version cash.z.ecc.android.Deps.Kotest.version apply false
    id("com.jfrog.bintray").version("1.+")
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://oss.sonatype.org/content/repositories/snapshots")
    }
}
