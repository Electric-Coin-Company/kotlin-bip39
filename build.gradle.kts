buildscript {
    dependencyLocking {
        lockAllConfigurations()
    }
}

dependencyLocking {
    lockAllConfigurations()
}

plugins {
    id("bip39.detekt-conventions")
    id("bip39.ktlint-conventions")
    alias(libs.plugins.kover)
    alias(libs.plugins.versions)
}

tasks {

    withType<com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask> {
        gradleReleaseChannel = "current"

        resolutionStrategy {
            componentSelection {
                all {
                    if (isNonStable(candidate.version) && !isNonStable(currentVersion)) {
                        reject("Unstable")
                    }
                }
            }
        }
    }
}

kover {
    isDisabled.set(!project.property("BIP39_IS_COVERAGE_ENABLED").toString().toBoolean())
    engine.set(kotlinx.kover.api.JacocoEngine(libs.versions.jacoco.get()))
}

val unstableKeywords = listOf("alpha", "beta", "rc", "m", "ea", "build")
fun isNonStable(version: String): Boolean {
    val versionLowerCase = version.toLowerCase()

    return unstableKeywords.any { versionLowerCase.contains(it) }
}