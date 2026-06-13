# Tip Calculator

A simple Android app for calculating tips, rounding the final total, and splitting the bill across multiple people.

## Features
- Enter a bill total with locale-aware decimal input
- Choose common tip presets or a custom tip percentage
- Split the bill between multiple people
- Round the total up or keep the exact amount
- Reset the calculator to default values
- Share a calculation summary
- Automatic light/dark theme support
- See total tip, total to pay, tip per person, and total per person

## Tech stack
- Kotlin 1.9.24
- Android Gradle Plugin 8.5.2
- Gradle 8.7
- Jetpack Compose + Material 3
- ViewModel + LiveData + SavedStateHandle
- Hilt

## Requirements
- Android Studio Jellyfish or newer
- JDK 17+
- Android SDK 34
- Minimum Android SDK 21

## Run locally
1. Open the project in Android Studio.
2. Let Gradle sync and download dependencies.
3. Run the `app` configuration on an emulator or device.

## Tests
```bash
./gradlew testDebugUnitTest
./gradlew connectedDebugAndroidTest
```

`connectedDebugAndroidTest` requires a running emulator or connected device.

## Release readiness
Run these checks before shipping:
```bash
./gradlew testDebugUnitTest
./gradlew connectedDebugAndroidTest
./gradlew lintDebug
./gradlew assembleDebug
```

CI runs unit tests, lint, and debug assembly via `.github/workflows/android.yml`.

## Module
- `app/` – Android application module

## License
See [`license`](license).
