# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

Single-module Android app (`:app`) written in Kotlin with Jetpack Compose + Material 3.  
Application id / namespace: `br.com.cesarsicas.androidmovieflix`.  
The app is a movie-streaming client for the ReactFlix platform, connecting to a Spring Boot backend at `http://10.0.2.2:8080` (emulator) or the configured base URL.

## Toolchain

- Kotlin `2.2.10`, Android Gradle Plugin `9.2.0`, Compose BOM `2026.02.01`
- `minSdk = 24`, `targetSdk = 36`, `compileSdk = 36` (with `minorApiLevel = 1`)
- Java source/target compatibility: 11
- Gradle build scripts use **Kotlin DSL** (`*.gradle.kts`)
- Dependencies and plugin versions live in `gradle/libs.versions.toml` — add libraries there first, then reference via `libs.*` in `app/build.gradle.kts`. Never hard-code versions in module build files.

## Common commands

Run from the `android-movie-flix/` directory using the Gradle wrapper:

| Task | Command |
| --- | --- |
| Full build | `./gradlew build` |
| Debug APK | `./gradlew :app:assembleDebug` |
| Release APK | `./gradlew :app:assembleRelease` |
| Install debug on device/emulator | `./gradlew :app:installDebug` |
| Unit tests | `./gradlew :app:testDebugUnitTest` |
| Instrumented tests (needs device) | `./gradlew :app:connectedDebugAndroidTest` |
| Lint | `./gradlew :app:lintDebug` |
| Clean | `./gradlew clean` |

## Architecture

Clean Architecture with three layers inside `br.com.cesarsicas.androidmovieflix/`:

```
data/
  remote/
    api/          # Retrofit interfaces (AuthApi, TitleApi, TransmissionApi, UserApi)
    dto/          # JSON response/request DTOs
    interceptor/  # AuthInterceptor — attaches Bearer token from TokenStore
    ChatSseClient.kt  # OkHttp SSE client for streaming AI chat
  local/
    TokenStore.kt     # DataStore — persists "token" (user) and "admin_token" (admin)
    ProfileCache.kt   # DataStore — cached user profile
    ReleasesCache.kt  # DataStore — cached movie releases
  mapper/         # Extension fns mapping DTOs → domain models
  repository/     # Implementations of domain repository interfaces

domain/
  model/          # Plain Kotlin data classes (MovieModel, TitleDetailsModel, …)
  repository/     # Interfaces (AuthRepository, TitleRepository, …)
  usecase/        # One-action use cases (LoginUseCase, GetReleasesUseCase, …)

presentation/
  admin/          # Admin screens (login, home, watch-party control, upload, new transmission)
  auth/           # AuthScreen + AuthViewModel
  browse/         # BrowseScreen + BrowseViewModel
  chat/           # ChatScreen + ChatViewModel (SSE streaming)
  common/         # Shared UI: AppTopBar, ChatFab, MovieItem, SectionContainer, HlsPlayer, …
  details/        # MovieDetailsScreen + MovieDetailsViewModel + ReviewDialog
  home/           # HomeScreen + HomeViewModel
  navigation/     # AppNavHost, UserNavGraph, AdminNavGraph, Routes, PlaceholderScreen
  person/         # PersonDetailsScreen + PersonDetailsViewModel
  profile/        # ProfileScreen + ProfileEditScreen + their ViewModels
  search/         # SearchResultScreen + SearchResultViewModel
  watchparty/     # WatchPartyScreen + WatchPartyViewModel

di/               # Hilt modules: NetworkModule, RepositoryModule, StorageModule
ui/theme/         # Color.kt, Theme.kt, Type.kt (VHS palette + M3 scheme)
MainActivity.kt   # Root Activity — hosts NavHost + Scaffold + bottom nav + FAB
MovieFlixApplication.kt  # @HiltAndroidApp entry point
```

## Dependency Injection

Hilt is used throughout.  
- `MovieFlixApplication` is annotated `@HiltAndroidApp`.  
- `MainActivity` is annotated `@AndroidEntryPoint`.  
- All ViewModels use `@HiltViewModel` and are injected with `hiltViewModel()` at call-site.  
- Modules in `di/` provide Retrofit, OkHttp, DataStore instances, and bind repository interfaces to their implementations.

## Navigation

- `AppNavHost` is the single `NavHost` composable inside `MainActivity`'s `Scaffold`.
- `UserNavGraph` contains all user-facing routes; `AdminNavGraph` is nested under `/admin/…`.
- `Routes.kt` holds all route strings as constants + helper builders (e.g. `Routes.titleDetails(id)`).
- Bottom nav (`NavigationBar` with 5 tabs) is shown only on top-level routes: Home, Browse, Live (Watch Party), Flix (Chat), Me (Profile).
- Bottom nav is **hidden** on: auth, title detail, search, person detail, admin screens, profile edit.
- The ✦ FAB (`ChatFab`) appears only on Home and Browse routes.
- Tab indicator is a square `Box` with a 2dp primary border (no M3 pill).

### Bottom nav tabs

| Tab | Icon | Route |
|-----|------|-------|
| Home | ⌂ | `Routes.HOME` |
| Browse | ▦ | `Routes.BROWSE` |
| Live | ● | `Routes.WATCH_PARTY` |
| Flix | ✦ | `Routes.CHAT` |
| Me | ◉ | `Routes.PROFILE` |

## VHS / Material 3 Theme

### Design language rules
- **0 border radius everywhere** — `RoundedCornerShape(0.dp)` on cards, buttons, chips, inputs, FAB.
- Buttons: uppercase, `FontFamily.Monospace`, 1.5dp border for outlined variants.
- Section titles: numbered prefix (`01`, `02`…) + uppercase mono label + trailing `HorizontalDivider`.
- Sticker badges: `VhsSticker` (slight rotation via `graphicsLayer { rotationZ = -2f }`, primary fill).
- Pill tags: `VhsPillTag` (surfaceVariant fill, outlineVariant border, monospace).

### Key color tokens (dark theme)

| Role | Value |
|------|-------|
| `surface` | `#16140F` |
| `surface-1` (`surfaceContainer`) | `#1F1C14` |
| `surface-2` | `#28241A` |
| `onSurface` | `#F3EAD2` |
| `onSurfaceVariant` | `#C9BF9C` |
| `primary` | `#F5C542` (golden yellow) |
| `onPrimary` | `#1A1812` |
| `primaryContainer` | `#3A2F12` |
| `secondary` | `#5FD9D9` (cyan) |
| `tertiary` | `#D96AA6` (pink) |
| `error` | `#C8362D` |
| `outline` | `rgba(243,234,210,0.22)` |

Light theme primary is `#C8362D` (red); surface is `#F3EAD2`.

### Fonts (deferred)

Custom fonts — Bowlby One SC (display), VT323 (CRT/mono), IBM Plex Mono (labels) — are **not yet wired up**. Currently the code falls back to `FontFamily.Serif` and `FontFamily.Monospace`. Do not add these fonts unless explicitly requested.

## Known API pitfalls

- **`OutlinedButtonDefaults` does not exist** in Compose BOM `2026.02.01`. Use `ButtonDefaults.outlinedButtonColors(...)` instead. Using `OutlinedButtonDefaults` will cause an unresolved reference compile error.
- `Modifier.offset()` requires `import androidx.compose.foundation.layout.offset` — it is not included in the wildcard layout import.
- `SectionContainer` internally calls `SectionTitle`; do **not** call `SectionTitle` separately before `SectionContainer` or you will render the header twice.

## Authentication

Two separate token types stored in `TokenStore` (Jetpack DataStore):
- `"token"` — regular user JWT, attached by `AuthInterceptor` to all API requests.
- `"admin_token"` — admin JWT, used only in admin flows.

`RequireUserAuth` / `RequireAdminAuth` composables redirect unauthenticated users to the appropriate login screen.

## HLS Streaming

`HlsPlayer.kt` wraps Media3/ExoPlayer to play HLS streams. Used in `WatchPartyScreen` for the live channel feed. The stream URL is fetched via `GetLiveStreamUrlUseCase`.

## Chat / SSE

`ChatSseClient` uses OkHttp's `EventSource` to open a Server-Sent Events connection to the Spring backend's `/default/chat` proxy. The proxy forwards to the Python AI service. Incoming text chunks are appended to the last `ChatMessage` in `ChatViewModel`'s state.

## Screens

| Screen | File | Notes |
|--------|------|-------|
| Home | `home/HomeScreen.kt` | Marquee strip + featured 16:10 hero card + 2 horizontal movie rows |
| Auth | `auth/AuthScreen.kt` | VHS tape logo, toggle sign-in/sign-up, Google placeholder |
| Browse | `browse/BrowseScreen.kt` | Square filter chips, sort dropdown, genre expansion, 3-col grid, infinite scroll |
| Movie Details | `details/MovieDetailsScreen.kt` | Backdrop hero + 3-tab row (OVERVIEW / TRAILER / REVIEWS) + cast initials |
| Watch Party | `watchparty/WatchPartyScreen.kt` | Channel strip + CRT frame + HLS player + signal meter (24 bars) |
| Chat (Flix) | `chat/ChatScreen.kt` | SSE streaming, flat-edge bubbles, ▶ send button, suggestion chips |
| Person Details | `person/PersonDetailsScreen.kt` | Initials circle avatar + biography + filmography LazyColumn |
| Profile | `profile/ProfileScreen.kt` | User info, logout |
| Profile Edit | `profile/ProfileEditScreen.kt` | Update name/avatar |
| Search | `search/SearchResultScreen.kt` | Grid results for title search queries |
| Admin Login | `admin/AdminLoginScreen.kt` | Separate admin credentials flow |
| Admin Home | `admin/AdminHomeScreen.kt` | Admin dashboard |
| Admin Watch Party | `admin/AdminWatchPartyScreen.kt` | Manage active transmission |
| Upload Movie | `admin/UploadMovieScreen.kt` | Upload video file |
| New Transmission | `admin/NewTransmissionScreen.kt` | Schedule a new live stream |

## Shared UI components (`presentation/common/`)

| Component | Purpose |
|-----------|---------|
| `AppTopBar` | Compact VHS top bar — "REACTFLIX" title, animated search expand, profile icon |
| `ChatFab` | Square ✦ FAB — primary bg, 2dp border, 4dp shadow |
| `MovieItem` | Grid card — poster + 1dp outline border + monospace year/rating below title |
| `SectionContainer` | Horizontal scroll row with numbered `SectionTitle` + trailing divider |
| `HlsPlayer` | ExoPlayer-backed HLS player composable |
| `Banner` | Toast-style banner for in-app messages |
| `UiState` | Sealed class: `Loading`, `Success<T>`, `Error` |

## Conventions

- UI is Compose-only — no XML layouts.
- All screens are wrapped in `AndroidMovieFlixTheme { … }` via the root `Scaffold` in `MainActivity`.
- `MainActivity` uses `enableEdgeToEdge()`; preserve `innerPadding` from the `Scaffold` when adding content.
- Release build has `isMinifyEnabled = false`; update `proguard-rules.pro` if you enable R8.
- `UiState` sealed class is the standard pattern for screen state in all ViewModels.
- `@OptIn(ExperimentalMaterial3Api::class)` is required for `SecondaryTabRow` (used in MovieDetails) and `RangeSlider` (used in Browse).
