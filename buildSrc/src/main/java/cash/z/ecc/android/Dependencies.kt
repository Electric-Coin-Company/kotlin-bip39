package cash.z.ecc.android

object Deps {
    // For use in the top-level build.gradle which gives an error when provided
    // `Deps.Kotlin.version` directly
    const val kotlinVersion = "1.3.72"
    const val group = "cash.z.ecc.android"
    const val versionName = "1.0.0-beta06"
    const val description = "A concise implementation of BIP-0039 in Kotlin for Android."

    object Kotlin : Version(kotlinVersion) {
        val STDLIB =        "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"
    }

    object Kotest : Version("4.0.5") {
        val RUNNER =        "io.kotest:kotest-runner-junit5-jvm:$version"
        val ASSERTIONS =    "io.kotest:kotest-assertions-core-jvm:$version"
        val PROPERTY =      "io.kotest:kotest-property-jvm:$version"
    }

    object Square : Version("1.9.2") {
        val MOSHI =         "com.squareup.moshi:moshi:$version"
        val MOSHI_KOTLIN =  "com.squareup.moshi:moshi-kotlin:$version"
    }
}

open class Version(@JvmField val version: String)
