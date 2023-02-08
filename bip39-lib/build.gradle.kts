import java.util.Base64

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    alias(libs.plugins.dokka)
    id("bip39.kotlin-multiplatform-conventions")
    id("bip39.dependency-conventions")
    id("maven-publish")
    id("signing")
}

val enableNative = project.property("NATIVE_TARGETS_ENABLED").toString().toBoolean()
val nativeTargets = if (enableNative) arrayOf(
    "linuxX64",
    "macosX64", "macosArm64",
    "iosArm64", "iosX64", "iosSimulatorArm64",
    "tvosArm64", "tvosX64", "tvosSimulatorArm64",
    "watchosArm32", "watchosArm64", "watchosX86", "watchosX64", "watchosSimulatorArm64",
    "mingwX64"
) else arrayOf()

kotlin {
    jvm {
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    js(IR) {
        browser() // to compile for the web
        nodejs() // to compile against node
    }
    for (target in nativeTargets) {
        targets.add(presets.getByName(target).createTarget(target))
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.kotest.framework.engine)
                implementation(libs.kotest.assertion)
                implementation(libs.kotest.property)
            }
        }
        val jvmMain by getting {
            dependencies {
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.kotest.runner.junit5)
            }
        }
        val nonJvmMain by creating {
            dependsOn(commonMain)
            dependencies {
                implementation(libs.com.squareup.okio)
            }
        }
        val jsMain by getting {
            dependsOn(nonJvmMain)
        }
        val nativeMain by creating {
            dependsOn(nonJvmMain)
        }
        val unixMain by creating {
            dependsOn(nonJvmMain)
        }
        for (target in nativeTargets) {
            when (target) {
                "mingwX64" ->
                    getByName("${target}Main").dependsOn(nativeMain)
                else ->
                    getByName("${target}Main").dependsOn(unixMain)
            }
        }
    }
}

tasks {
    val dokkaOutputDir = File(buildDir, "dokka")

    dokkaHtml.configure {
        outputDirectory.set(dokkaOutputDir)
    }

    val deleteDokkaOutputDir = register<Delete>("deleteDokkaOutputDirectory") {
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
            groupId = "cash.z.ecc.android"
            version = if (isSnapshot) {
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
        val mavenUrl = if (isSnapshot) {
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

    val signingKey = run {
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
