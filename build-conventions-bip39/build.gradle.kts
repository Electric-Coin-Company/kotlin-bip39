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
    val rootProperties = getRootProperties()
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${rootProperties.getProperty("KOTLIN_VERSION")}")
    implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:${rootProperties.getProperty("DETEKT_VERSION")}")
}

// A slightly gross way to use the root gradle.properties as the single source of truth for version numbers
fun getRootProperties() =
    org.jetbrains.kotlin.konan.properties.loadProperties(File(project.projectDir.parentFile, "gradle.properties").path)
