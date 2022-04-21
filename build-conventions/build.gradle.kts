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

// Per conversation in the KotlinLang Slack, Gradle uses Java 8 compatibility internally
// for all build scripts.
// https://kotlinlang.slack.com/archives/C19FD9681/p1636632870122900?thread_ts=1636572288.117000&cid=C19FD9681
java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${libs.versions.kotlin.get()}")
}
