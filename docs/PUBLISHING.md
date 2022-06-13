# Overview
We aim for the main branch of the repository to always be in a releasable state.

Two types of artifacts can be published:
1. Snapshot — An unstable release of the SDK for testing
1. Release — A stable release of the SDK

Control of these modes of release is managed with a Gradle property `IS_SNAPSHOT`.

For both snapshot and release publishing, there are two ways to initiate deployment:
1. Automatically
2. Manually

This document will focus initially on the automated process, with a section at the end on manual process.  (The automated process more or less implements the manual process via GitHub Actions.)

# Automated Publishing
## Snapshots
All merges to the main branch trigger an automated [snapshot deployment](https://github.com/zcash/kotlin-bip39/actions/workflows/deploy-snapshot.yml).

Note that snapshots do not have a stable API, so clients should not depend on a snapshot.  The primary reason this is documented is for testing, e.g. before deploying a new production version of the library we may test against the snapshot first.

Snapshots can be consumed by:

1. Adding the snapshot repository
settings.gradle.kts:
```
dependencyResolutionManagement {
    repositories {
        maven("https://oss.sonatype.org/content/repositories/snapshots") {
            // Optional; ensures only explicitly declared dependencies come from this repository
            content {
                includeGroup("cash.z.ecc.android")
            }
        }
    }
}
```

2. Changing the dependency version to end with `-SNAPSHOT`

3. Rebuilding
`./gradlew assemble --refresh-dependencies`

Because Gradle caches dependencies and because multiple snapshots can be deployed under the same version number, using `--refresh-dependencies` is important to ensure the latest snapshot is pulled.

## Releases
Production releases can be consumed using the instructions in the [README.MD](../README.md).  Note that production releases can include alpha or beta designations.

Automated production releases still require a manual trigger.  To do a production release:
1. Update the CHANGELOG and MIGRATIONS.md for any new changes since the last production release.
1. Run the [release deployment](https://github.com/zcash/kotlin-bip39/actions/workflows/deploy-release.yml).
1. Confirm deployment succeeded by modifying the [Secant Android Wallet](https://github.com/zcash/secant-android-wallet) to consume the new version.
1. Create a new Git tag for the new release in this repository.
1. Create a new pull request bumping the version to the next version (this ensures that the next merge to the main branch creates a snapshot under the next version number).

# Manual Publishing
See [ci.md](ci.md), which describes the continuous integration workflow for deployment and describes the secrets that would need to be configured in a repository fork.

## One time only
* Set up environment to [compile the SDK](https://github.com/zcash/zcash-android-wallet-sdk/#compiling-sources)
* Copy the GPG key to a directory with proper permissions (chmod 600). Note: If you'd like to quickly publish locally without subsequently publishing to Maven Central, configure a Gradle property `RELEASE_SIGNING_ENABLED=false`
* Create file `~/.gradle/gradle.properties` per the [instructions in this guide](https://proandroiddev.com/publishing-a-maven-artifact-3-3-step-by-step-instructions-to-mavencentral-publishing-bd661081645d)
  * add your sonotype credentials with these properties
      * `mavenCentralUsername`
      * `mavenCentralPassword`
  * point it to the GPG key with these properties
     * `signing.keyId`
     * `signing.password`
     * `signing.secretKeyRingFile`

## Every time
1. Update the [build number](https://github.com/zcash/zcash-android-wallet-sdk/blob/master/gradle.properties) and the [CHANGELOG](https://github.com/zcash/zcash-android-wallet-sdk/blob/master/CHANGELOG.md).  For release builds, suffix the Gradle invocations below with `-PIS_SNAPSHOT=false`.
3. Build locally
    * This will install the files in your local maven repo at `~/.m2/repository/cash/z/ecc/android/`
```zsh
./gradlew publishToMavenLocal
```
4. Publish via the following command:
```zsh
# This uploads the file to sonotype’s staging area
./gradlew publish --no-daemon --no-parallel
```
5. Deploy to maven central:
```zsh
# This closes the staging repository and releases it to the world
./gradlew closeAndReleaseRepository
```

Note:
Our existing artifacts can be found here and here:
https://search.maven.org/artifact/cash.z.ecc.android/kotlin-bip39

