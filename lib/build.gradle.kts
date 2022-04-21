plugins {
    alias(libs.plugins.kotlin)
    id("java-library")
    alias(libs.plugins.dokka)
    alias(libs.plugins.publish)
}

group = project.property("GROUP").toString()
version = project.property("VERSION_NAME").toString()

sourceSets {
    main {
        java {
            setSrcDirs(listOf("src/jvmMain/kotlin"))
        }
    }

    test {
        java {
            setSrcDirs(listOf("src/jvmTest/kotlin", "src/jvmTest/resources"))
        }
    }
}

kotlin {
    jvmToolchain {
        // This should be set lower for Android, although there's no compatible JVM for Apple Silicon
        (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(11))
    }
}

dependencies {
    // Tests
    testImplementation(kotlin("test"))
    testImplementation(libs.kotest.runner)
    testImplementation(libs.kotest.assertion)
    testImplementation(libs.kotest.property)
    testImplementation(libs.moshi.core)
    testImplementation(libs.moshi.kotlin)
}

tasks.withType<Test> {
    useJUnitPlatform()
}
