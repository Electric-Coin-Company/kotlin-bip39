import cash.z.ecc.android.Deps

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.dokka")
    id("java-library")
    id("com.vanniktech.maven.publish")
}

group = project.property("GROUP").toString()
version = project.property("VERSION_NAME").toString()


kotlin {
    jvmToolchain {
        // This should be set lower for Android, although there's no compatible JVM for Apple Silicon
        (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(11))
    }
}

dependencies {
    // Tests
    testImplementation(kotlin("test"))
    testImplementation(Deps.Kotest.RUNNER)
    testImplementation(Deps.Kotest.ASSERTIONS)
    testImplementation(Deps.Kotest.PROPERTY)
    testImplementation(Deps.Square.MOSHI)
    testImplementation(Deps.Square.MOSHI_KOTLIN)
}

tasks.withType<Test> {
    useJUnitPlatform()
}
