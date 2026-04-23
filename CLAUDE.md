# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

Single-module Android app (`:app`) written in Kotlin with Jetpack Compose + Material 3. Application id / namespace: `br.com.cesarsicas.androidmovieflix`. The codebase is currently a fresh Android Studio scaffold (a `MainActivity` hosting a `Greeting` composable under `AndroidMovieFlixTheme`) — no domain/feature code exists yet, so new features start from an empty canvas.

## Toolchain

- Kotlin `2.2.10`, Android Gradle Plugin `9.2.0`, Compose BOM `2026.02.01`
- `minSdk = 24`, `targetSdk = 36`, `compileSdk = 36` (with `minorApiLevel = 1`)
- Java source/target compatibility: 11
- Gradle build scripts use **Kotlin DSL** (`*.gradle.kts`)
- Dependencies and plugin versions live in the version catalog `gradle/libs.versions.toml` — add libraries there first, then reference via `libs.*` in `app/build.gradle.kts`. Do not hard-code versions in module build files.

## Common commands

Run from repo root using the Gradle wrapper (`./gradlew`):

| Task | Command |
| --- | --- |
| Full build | `./gradlew build` |
| Debug APK | `./gradlew :app:assembleDebug` |
| Release APK | `./gradlew :app:assembleRelease` |
| Install debug on connected device/emulator | `./gradlew :app:installDebug` |
| Unit tests (JVM, `src/test/`) | `./gradlew :app:testDebugUnitTest` |
| Single unit test | `./gradlew :app:testDebugUnitTest --tests "br.com.cesarsicas.androidmovieflix.ExampleUnitTest.addition_isCorrect"` |
| Instrumented tests (`src/androidTest/`, needs device/emulator) | `./gradlew :app:connectedDebugAndroidTest` |
| Lint | `./gradlew :app:lintDebug` |
| Clean | `./gradlew clean` |

## Source layout

- `app/src/main/java/br/com/cesarsicas/androidmovieflix/` — application code; `ui/theme/` holds the Compose theme (`Theme.kt`, `Color.kt`, `Type.kt`) used by `MainActivity`.
- `app/src/test/` — JVM unit tests (JUnit 4).
- `app/src/androidTest/` — instrumented tests (AndroidJUnit4 + Compose UI test via `androidx-compose-ui-test-junit4`).
- `app/src/main/res/` — standard Android resources.

## Conventions

- UI is Compose-only (`buildFeatures { compose = true }`, no View/XML layouts). Wrap new screens in `AndroidMovieFlixTheme { ... }` so the app theme applies consistently.
- `MainActivity` uses `enableEdgeToEdge()` and a root `Scaffold` — preserve edge-to-edge + the `Scaffold` `innerPadding` when adding top-level content.
- Release build type currently has `isMinifyEnabled = false`; if you turn on R8/minification, update `app/proguard-rules.pro` accordingly.
