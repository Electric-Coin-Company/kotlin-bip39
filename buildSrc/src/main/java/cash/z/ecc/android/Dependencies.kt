package cash.z.ecc.android

object Deps {
    // For use in the top-level build.gradle which gives an error when provided
    // `Deps.Kotlin.version` directly
    const val kotlinVersion = "1.6.20-M1"

    object Kotest : Version("5.2.1") {
        val RUNNER =        "io.kotest:kotest-runner-junit5:$version"
        val ASSERTIONS =    "io.kotest:kotest-assertions-core:$version"
        val FRAMEWORK_ENG = "io.kotest:kotest-framework-engine:$version"
        val PROPERTY =      "io.kotest:kotest-property:$version"
    }

    object SquareOkio : Version("3.0.0"){
        val OKIO =          "com.squareup.okio:okio:$version"
    }
}

open class Version(@JvmField val version: String)
