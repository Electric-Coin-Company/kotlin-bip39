# Continuous Integration
Continuous integration is set up with GitHub Actions.  The workflows are defined in this repo under [/.github/workflows](../.github/workflows).

Workflows exist for:
 * Pull request - On pull request, static analysis and testing is performed.
 * Snapshot deployment - On merge to the main branch, a snapshot release is deployed to Maven Central.  Concurrency limits are in place, to ensure that only one snapshot deployment can happen at a time.
 * Release deployment - Manually invoked workflow to deploy to Maven Central.  Concurrency limits are in place, to ensure that only one release deployment can happen at a time.
 * Unwedge — If Snapshot deployment fails, it will often be due to multiple unclosed repositories.  This workflow can take a given open repository name and attempt to close it.
 
## Setup
When forking this repository, some secrets need to be defined to set up new continuous integration builds.

The secrets passed to GitHub Actions then map to Gradle properties set up within our build scripts.  Necessary secrets are documented at the top of each GitHub workflow yml file, as well as reiterated here.

### Pull request
No configuration is required.

### Snapshot deployment
* `MAVEN_CENTRAL_USERNAME` — Username for Maven Central, which maps to the Gradle property `mavenCentralUsername`.
* `MAVEN_CENTRAL_PASSWORD` — Password for Maven Central, which maps to the Gradle property `mavenCentralPassword`.

GPG keys are not needed for snapshot deployment.

Note: For documentation on the Gradle properties for Maven deployment, see [Gradle Maven Publish Plugin](https://github.com/vanniktech/gradle-maven-publish-plugin).

Note: Snapshot builds are configured with a Gradle property `IS_SNAPSHOT`.  The workflow automatically sets this property to true for snapshot deployments.  This will suffix the version with `-snapshot` and will upload to the snapshot repository.

### Release deployment
* `MAVEN_CENTRAL_USERNAME` — Username for Maven Central, which maps to the Gradle property `mavenCentralUsername`.
* `MAVEN_CENTRAL_PASSWORD` — Password for Maven Central, which maps to the Gradle property `mavenCentralPassword`.
* `MAVEN_SIGNING_KEYRING_FILE_BASE64` — GPG keyring file, base64 encoded.  Maps to Gradle property `signing.secretKeyRingFile`.
* `MAVEN_SIGNING_KEY_ID` — Name of key inside GPG keyring file.  Maps to Gradle property `signing.keyId`.
* `MAVEN_SIGNING_PASSWORD` — Password for key inside GPG keyring file.  Maps to Gradle property `signing.password`.

Note: For documentation on the Gradle properties for Maven deployment, see [Gradle Maven Publish Plugin](https://github.com/vanniktech/gradle-maven-publish-plugin).

Note: Snapshot builds are configured with a Gradle property `IS_SNAPSHOT`.  The workflow automatically sets this property to false for release deployments.

### Unwedge
* `MAVEN_CENTRAL_USERNAME` — Username for Maven Central, which maps to the Gradle property `mavenCentralUsername`.
* `MAVEN_CENTRAL_PASSWORD` — Password for Maven Central, which maps to the Gradle property `mavenCentralPassword`.