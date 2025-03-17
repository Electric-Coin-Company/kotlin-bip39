# Changelog
All notable changes to this library will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this library adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Changed
- Gradle 8.13
- Kotlin 2.0.20
- Other dependency update

### Fixed
- The build-time warning about deprecated `KotlinTargetPreset.createTarget()` has been resolved

## [1.0.8] - 2024-04-14

### Changed
- Gradle 8.7
- Kotlin 1.9.23
- Other dependency update

## [1.0.7] - 2024-01-02

### Changed
- Gradle 8.5
- Kotlin 1.9.21
- Other dependency update

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
