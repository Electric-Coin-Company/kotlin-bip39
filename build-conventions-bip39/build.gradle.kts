plugins {
    `kotlin-dsl`
}

buildscript {
    dependencyLocking {
        // This property is treated specially, as it is not defined by default in the root gradle.properties
        // and declaring it in the root gradle.properties is ignored by included builds. This only picks up
        // a value declared as a system property, a command line argument, or an environment variable.
        val isDependencyLockingEnabled =
            if (project.hasProperty("ZCASH_IS_DEPENDENCY_LOCKING_ENABLED")) {
                project.property("ZCASH_IS_DEPENDENCY_LOCKING_ENABLED").toString().toBoolean()
            } else {
                true
            }
        if (isDependencyLockingEnabled) {
            lockAllConfigurations()
        }
    }
}

dependencyLocking {
    // This property is treated specially, as it is not defined by default in the root gradle.properties
    // and declaring it in the root gradle.properties is ignored by included builds. This only picks up
    // a value declared as a system property, a command line argument, or an environment variable.
    val isDependencyLockingEnabled =
        if (project.hasProperty("ZCASH_IS_DEPENDENCY_LOCKING_ENABLED")) {
            project.property("ZCASH_IS_DEPENDENCY_LOCKING_ENABLED").toString().toBoolean()
        } else {
            true
        }
    if (isDependencyLockingEnabled) {
        lockAllConfigurations()
    }
}

dependencies {
    implementation(libs.kotlin.gradle)
    implementation(libs.kotlinx.kover.gradle)
    implementation(libs.detekt.gradle)
}
