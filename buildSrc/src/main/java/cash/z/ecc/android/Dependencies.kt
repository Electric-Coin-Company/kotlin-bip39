package cash.z.ecc.android

object Deps {
    // For use in the top-level build.gradle which gives an error when provided
    // `Deps.Kotlin.version` directly
    const val kotlinVersion = "1.6.20-M1"

    object Kotlin : Version(kotlinVersion) {
        val STDLIB =        "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"
    }

    object Kotest : Version("5.2.2") {
        val RUNNER =        "io.kotest:kotest-runner-junit5:$version"
        val ASSERTIONS =    "io.kotest:kotest-assertions-core:$version"
        val PROPERTY =      "io.kotest:kotest-property:$version"
    }

    object SquareMoshi : Version("1.13.0") {
        val MOSHI =         "com.squareup.moshi:moshi:$version"
        val MOSHI_KOTLIN =  "com.squareup.moshi:moshi-kotlin:$version"
    }

    object SquareOkio : Version("3.0.0"){
        val OKIO =          "com.squareup.okio:okio:$version"
    }
}

open class Version(@JvmField val version: String)
