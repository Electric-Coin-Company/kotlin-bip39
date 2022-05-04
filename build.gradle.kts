buildscript {
    dependencyLocking {
        lockAllConfigurations()
    }
}

dependencyLocking {
    lockAllConfigurations()
}

plugins {
    id("bip39.ktlint-conventions")
    alias(libs.plugins.detekt)
}

tasks {
    register("detektAll", io.gitlab.arturbosch.detekt.Detekt::class) {
        parallel = true
        setSource(files(projectDir))
        include("**/*.kt")
        //include("**/*.kts")
        exclude("**/resources/**")
        exclude("**/build/**")
        exclude("**/commonTest/**")
        exclude("**/jvmTest/**")
        exclude("**/androidTest/**")
        config.setFrom(files("${rootProject.projectDir}/tools/detekt.yml"))
        baseline.set(file("$rootDir/tools/detekt-baseline.xml"))
        buildUponDefaultConfig = true
    }
}
