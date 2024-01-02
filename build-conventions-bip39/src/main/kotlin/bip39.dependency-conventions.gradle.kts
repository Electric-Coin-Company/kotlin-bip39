dependencyLocking {
    lockAllConfigurations()
}

tasks {
    register("resolveAll") {
        dependsOn(":commonizeNativeDistribution")
        doLast {
            configurations.filter {
                // Add any custom filtering on the configurations to be resolved
                it.isCanBeResolved
            }.forEach { it.resolve() }
        }
    }
}
