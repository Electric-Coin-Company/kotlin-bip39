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
    ktlint("com.pinterest:ktlint:${someVersion}") {
        attributes {
            attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named<Bundling>(Bundling.EXTERNAL))
        }
    }
}

tasks {
    val editorConfigFile = rootProject.file(".editorconfig")
    val ktlintArgs = listOf("**/src/**/*.kt", "!**/build/**.kt", "--editorconfig=$editorConfigFile")

    register("ktlint", org.gradle.api.tasks.JavaExec::class) {
        description = "Check code style with ktlint"
        classpath = ktlint
        mainClass.set("com.pinterest.ktlint.Main")
        args = ktlintArgs
    }

    register("ktlintFormat", org.gradle.api.tasks.JavaExec::class) {
        // Workaround for ktlint bug; force to run on an older JDK
        // https://github.com/pinterest/ktlint/issues/1274
        javaLauncher.set(javaToolchains.launcherFor {
            languageVersion.set(JavaLanguageVersion.of(JavaVersion.VERSION_11.majorVersion))
        })

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