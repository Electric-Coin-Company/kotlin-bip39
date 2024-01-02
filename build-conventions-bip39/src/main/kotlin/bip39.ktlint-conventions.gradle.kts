// Workaround for Version Catalogs
// https://github.com/gradle/gradle/issues/15383#issuecomment-1013300927
val catalogs = extensions
    .getByType<VersionCatalogsExtension>()
val someVersion = catalogs.named("libs").findVersion("ktlint").get().requiredVersion

plugins {
    id("java")
}

val ktlint by configurations.creating

dependencies {
    ktlint("com.pinterest.ktlint:ktlint-cli:${someVersion}") {
        attributes {
            attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named<Bundling>(Bundling.EXTERNAL))
        }
    }
}

tasks {
    val editorConfigFile = rootProject.file("tools/.editorconfig")
    val ktlintArgs = listOf("**/src/**/*.kt", "!**/build/**.kt", "--editorconfig=$editorConfigFile")

    register("ktlint", org.gradle.api.tasks.JavaExec::class) {
        description = "Check code style with ktlint"
        classpath = ktlint
        mainClass.set("com.pinterest.ktlint.Main")
        args = ktlintArgs
    }

    register("ktlintFormat", org.gradle.api.tasks.JavaExec::class) {
        // https://github.com/pinterest/ktlint/issues/1195#issuecomment-1009027802
        jvmArgs("--add-opens", "java.base/java.lang=ALL-UNNAMED")

        description = "Apply code style formatting with ktlint"
        classpath = ktlint
        mainClass.set("com.pinterest.ktlint.Main")
        args = listOf("-F") + ktlintArgs
    }
}

java {
    val javaVersion = JavaVersion.toVersion(project.property("JVM_TARGET").toString())
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
}