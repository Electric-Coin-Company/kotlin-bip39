dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "kotlin-bip39"
include(":lib")

includeBuild("build-conventions")
