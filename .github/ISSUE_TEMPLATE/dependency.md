---
name: Dependency update
about: Update existing dependency to a new version.
title: ''
labels: dependencies
assignees: ''

---

For a Gradle dependency:
1. Update the dependency version in the root `gradle.properties`
1. Update the dependency locks
<!--    1. For Gradle plugins: `./gradlew dependencies --write-locks` -->
<!--    1. For Gradle dependencies: `./gradlew resolveAll --write-locks` -->
<!--1. Verify no unexpected entries appear in the lockfiles. _A supply chain attack could occur during this stage. The lockfile narrows the supply chain attack window to this very moment (as opposed to every time a build occurs)_ -->
1. Are there any new APIs or possible migrations for this dependency?

For Gradle itself:
1. Update the Gradle version in `gradle/wrapper/gradle-wrapper.properties`
1. Update the [Gradle SHA](https://gradle.org/release-checksums/) in `gradle/wrapper/gradle-wrapper.properties`
1. Update the Gradle wrapper by running `./gradlew wrapper`
1. Re-add the [Gradle SHA](https://gradle.org/release-checksums/) to `gradle/wrapper/gradle-wrapper.properties`
1. Are there any new APIs or possible migrations?
