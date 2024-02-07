import java.util.*

plugins {
    // https://github.com/gradle/gradle/issues/20084#issuecomment-1060822638
    id(libs.plugins.kotlin.multiplatform.get().pluginId)
    alias(libs.plugins.dokka)
    id("bip39.kotlin-multiplatform-conventions")
    id("bip39.dependency-conventions")

    // https://github.com/gradle/gradle/issues/20084#issuecomment-1060822638
    id(libs.plugins.kotlinx.kover.get().pluginId)
    id("bip39.coverage-conventions")

    id("maven-publish")
    id("signing")
}

val enableNative = project.property("NATIVE_TARGETS_ENABLED").toString().toBoolean()
val enableJs = project.property("JS_TARGET_ENABLED").toString().toBoolean()
val nativeTargets =
    if (enableNative) {
        arrayOf(
            "linuxX64",
            "macosX64", "macosArm64",
            "iosArm64", "iosX64", "iosSimulatorArm64",
            "tvosArm64", "tvosX64", "tvosSimulatorArm64",
            "watchosArm32", "watchosArm64", "watchosX64", "watchosSimulatorArm64",
            "mingwX64",
        )
    } else {
        arrayOf()
    }

kotlin {
    jvm {
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    if (enableJs) {
        js(IR) {
            browser {
                testTask {
                    useMocha {
                        // Needed due to: https://github.com/square/okio/issues/1206
                        timeout = "60s"
                    }
                }
            }
            nodejs {
                testTask {
                    useMocha {
                        // Needed due to: https://github.com/square/okio/issues/1206
                        timeout = "60s"
                    }
                }
            }
        }
    }
    if (enableNative) {
        for (target in nativeTargets) {
            targets.add(presets.getByName(target).createTarget(target))
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        if (enableNative || enableJs) {
            val nonJvmMain by creating {
                dependsOn(commonMain)
                dependencies {
                    implementation(libs.com.squareup.okio)
                }
            }

            if (enableJs) {
                jsMain {
                    dependsOn(nonJvmMain)
                }
            }

            if (enableNative) {
                val mingwMain by creating {
                    dependsOn(nonJvmMain)
                }
                val unixMain by creating {
                    dependsOn(nonJvmMain)
                }
                for (target in nativeTargets) {
                    when (target) {
                        "mingwX64" ->
                            getByName("${target}Main").dependsOn(mingwMain)

                        else ->
                            getByName("${target}Main").dependsOn(unixMain)
                    }
                    getByName("${target}Test").dependsOn(commonTest)
                }
            }
        }
    }
}

tasks {
    val dokkaOutputDir = layout.buildDirectory.dir("dokka").get().asFile

    dokkaHtml.configure {
        outputDirectory.set(dokkaOutputDir)
    }

    val deleteDokkaOutputDir =
        register<Delete>("deleteDokkaOutputDirectory") {
            delete(dokkaOutputDir)
        }

    register<Jar>("javadocJar") {
        dependsOn(deleteDokkaOutputDir, dokkaHtml)
        archiveClassifier.set("javadoc")
        from(dokkaOutputDir)
    }
}

val publicationVariant = "release"
val myVersion = project.property("LIBRARY_VERSION").toString()
val myArtifactId = "kotlin-bip39"
val isSnapshot = project.property("IS_SNAPSHOT").toString().toBoolean()

publishing {
    publications {
        withType<MavenPublication> {
            artifact(tasks.getByName("javadocJar"))

            // Artifact id is tricky.  Gradle uses the project name by default, but Kotlin multiplatform needs to add
            // platform specific suffixes.  Doing a partial replacement is the way to rename the artifact.
            artifactId = artifactId.replace(project.name, myArtifactId)
            groupId = "io.github.luca992.cash.z.ecc.android"
            version =
                if (isSnapshot) {
                    "$myVersion-SNAPSHOT"
                } else {
                    myVersion
                }

            pom {
                name.set("Kotlin BIP-39")
                description.set("A concise implementation of BIP-0039 in Kotlin.")
                url.set("https://github.com/zcash/kotlin-bip39/")
                inceptionYear.set("2020")
                scm {
                    url.set("https://github.com/zcash/kotlin-bip39/")
                    connection.set("scm:git:git://github.com/zcash/kotlin-bip39.git")
                    developerConnection.set("scm:git:ssh://git@github.com/zcash/kotlin-bip39.git")
                }
                developers {
                    developer {
                        id.set("zcash")
                        name.set("Zcash")
                        url.set("https://github.com/zcash/")
                    }
                }
                licenses {
                    license {
                        name.set("The MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                        distribution.set("repo")
                    }
                }
            }
        }
    }
    repositories {
        val mavenUrl =
            if (isSnapshot) {
                project.property("ZCASH_MAVEN_PUBLISH_SNAPSHOT_URL").toString()
            } else {
                project.property("ZCASH_MAVEN_PUBLISH_RELEASE_URL").toString()
            }
        val mavenPublishUsername = project.property("ZCASH_MAVEN_PUBLISH_USERNAME").toString()
        val mavenPublishPassword = project.property("ZCASH_MAVEN_PUBLISH_PASSWORD").toString()

        mavenLocal {
            name = "MavenLocal"
        }
        maven(mavenUrl) {
            name = "MavenCentral"
            credentials {
                username = mavenPublishUsername
                password = mavenPublishPassword
            }
        }
    }
}

signing {
    // Maven Central requires signing for non-snapshots
    isRequired = !isSnapshot

    val signingKey =
        run {
            val base64EncodedKey = project.property("ZCASH_ASCII_GPG_KEY").toString()
            if (base64EncodedKey.isNotEmpty()) {
                val keyBytes = Base64.getDecoder().decode(base64EncodedKey)
                String(keyBytes)
            } else {
                ""
            }
        }

    if (signingKey.isNotEmpty()) {
        useInMemoryPgpKeys(signingKey, "")
    }

    sign(publishing.publications)
}

// Workaround for:
// - https://youtrack.jetbrains.com/issue/KT-46466
// - https://github.com/gradle/gradle/issues/26091
// A problem was found with the configuration of task ':bip39-lib:signKotlinMultiplatformPublication' (type 'Sign').
// Gradle detected a problem with the following location:
// '/home/runner/work/kotlin-bip39/kotlin-bip39/bip39-lib/build/libs/bip39-lib-javadoc.jar.asc'.
// Reason: Task ':bip39-lib:publishJvmPublicationToMavenLocalRepository' uses this output of task
// ':bip39-lib:signKotlinMultiplatformPublication' without declaring an explicit or implicit
// dependency. This can lead to incorrect results being produced, depending on what order the tasks are executed.
val signingTasks = tasks.withType<Sign>()
tasks.withType<AbstractPublishToMaven>().configureEach {
    dependsOn(signingTasks)
}
