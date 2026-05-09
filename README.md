# ReactFlix Android

Android client for the ReactFlix movie-streaming platform. Built with Jetpack Compose and Material 3, styled with a VHS/retro aesthetic.

## Tech Stack

| Area | Library |
|------|---------|
| UI | Jetpack Compose + Material 3 (BOM 2026.02.01) |
| Language | Kotlin 2.2.10 |
| Architecture | Clean Architecture (data / domain / presentation) |
| DI | Hilt |
| Navigation | Navigation Compose |
| Networking | Retrofit + OkHttp |
| Image loading | Coil |
| Video playback | Media3 / ExoPlayer (HLS) |
| Local storage | Jetpack DataStore |
| Build | Gradle Kotlin DSL + version catalog |
| Min SDK | 24 (Android 7.0) |
| Target SDK | 36 |

## Architecture

```
data/
  remote/      Retrofit APIs, DTOs, Auth interceptor, SSE chat client
  local/       DataStore — JWT tokens, profile cache, releases cache
  mapper/      DTO → domain model mappers
  repository/  Repository implementations

domain/
  model/       Pure Kotlin data classes
  repository/  Repository interfaces
  usecase/     Single-responsibility use cases

presentation/
  common/      Shared composables (AppTopBar, ChatFab, MovieItem, SectionContainer, …)
  navigation/  NavHost, route graphs, Routes constants
  home/        Home screen
  auth/        Sign-in / Sign-up
  browse/      Browse & filter
  details/     Movie details (tabs: Overview / Trailer / Reviews)
  watchparty/  Live watch party (HLS player)
  chat/        AI chat — Flix (SSE streaming)
  person/      Person / cast detail
  profile/     User profile & edit
  search/      Search results
  admin/       Admin-only screens (login, dashboard, transmissions, upload)
```

## Screens

| Screen | Description |
|--------|-------------|
| Home | Marquee ticker + featured 16:10 hero card + two horizontal movie rows |
| Browse | Filter chips, sort dropdown, genre filters, rating range slider, infinite-scroll 3-column grid |
| Movie Details | Backdrop hero, tabbed content (overview / trailer / reviews), cast scroll |
| Watch Party | HLS live stream, CRT-style player frame, signal-strength meter, status card |
| Flix Chat | AI movie chatbot with SSE streaming, suggestion chips |
| Person Details | Cast/crew bio + filmography list |
| Profile | User info, edit profile, logout |
| Search | Full-text title search results |
| Auth | Sign-in / Sign-up with VHS tape logo |
| Admin | Transmission management, movie upload (separate admin token flow) |

## Navigation

A 5-tab `NavigationBar` at the bottom handles top-level navigation. The bottom bar and FAB are hidden on detail, auth, search, and admin screens.

| Tab | Route |
|-----|-------|
| ⌂ Home | `/home` |
| ▦ Browse | `/browse` |
| ● Live | `/watch-party` |
| ✦ Flix | `/chat` |
| ◉ Me | `/profile` |

## Design System

Material 3 + VHS/retro hybrid:

- **Hard edges** — `RoundedCornerShape(0.dp)` everywhere (cards, buttons, chips, inputs, FAB)
- **Dark primary**: golden yellow `#F5C542` / **Light primary**: red `#C8362D`
- **Surface dark**: `#16140F` / **On-surface dark**: `#F3EAD2`
- Section headers: numbered prefix (`01`, `02`…) + uppercase monospace label + divider
- Sticker badges with slight tilt, drop shadow
- CRT-style labels and decorative overlays (scanlines, tracking line, signal meter)

## Setup & Build

### Prerequisites

- Android Studio Meerkat (2024.3.1) or newer
- JDK 17
- The ReactFlix backend running on port `8080` (see `backend/` in the monorepo root)

### Clone & configure

```bash
git clone <repo-url>
cd movie-flix/android-movie-flix
```

The base URL is set in `NetworkModule.kt`. For a local emulator it defaults to `http://10.0.2.2:8080`. For a physical device, update it to your machine's LAN IP.

### Build

```bash
# Debug APK
./gradlew :app:assembleDebug

# Install on connected emulator / device
./gradlew :app:installDebug
```

### Run tests

```bash
# Unit tests
./gradlew :app:testDebugUnitTest

# Instrumented tests (requires running emulator or device)
./gradlew :app:connectedDebugAndroidTest
```

## Backend dependency

This app requires the Spring Boot backend (`backend/` in the monorepo) to be running. The backend exposes:

- REST endpoints for auth, movies, users, reviews, transmissions
- SSE proxy at `/default/chat` — forwards to the Python AI service
- HLS stream URL for live watch parties

The Python AI service (`python-agent-flix/`) is optional — chat will fail gracefully if it is not running.
