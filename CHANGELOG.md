# Changelog
All notable changes to this library will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this library adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [1.0.6] - 2023-09-27
- Gradle 8.3
- Updated other internally used dependencies

## [1.0.5] - 2023-05-22
- Improved performance and memory usage
- Kotlin 1.8.21
- Internal changes to better support Kotlin Multiplatform

## [1.0.4] - 2022-07-29
- Fixed Maven publishing error in 1.0.3 release

## [1.0.3] - 2022-07-26
- Kotlin 1.7.10
- Internal changes to support multiplatform in the future (consumers using Maven instead of Gradle may need to suffix -jvm to the artifact name)
- Snapshot builds are available prior to final release

## [1.0.2] - 2022-06-14
- Publish to Maven Central due to Jcenter deprecation

## [1.0.1] - 2020-09-19
- First non-beta release!  
- New: Adds support for older devices that are missing crypto libraries.
- New: Updates to v1.4.10 of kotlin.
- Fix: Bug in publishing script.
