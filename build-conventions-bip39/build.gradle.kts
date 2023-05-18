plugins {
    `kotlin-dsl`
}

buildscript {
    dependencyLocking {
        lockAllConfigurations()
    }
}

dependencyLocking {
    lockAllConfigurations()
}

dependencies {
    implementation(libs.kotlin.gradle)
    implementation(libs.kotlinx.kover.gradle)
    implementation(libs.detekt.gradle)
}
