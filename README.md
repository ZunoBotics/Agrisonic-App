# Agrisonic Android App

Android application for Agrisonic - an intelligent agricultural platform serving Ugandan farmers with comprehensive farming solutions.

## Overview

This is the native Android version of the Agrisonic web app, built in Kotlin using modern Android development practices. The app provides farmers with:

- AI-powered crop prediction
- Real-time weather forecasts
- Market price information
- Pest and disease detection
- IoT sensor integration
- Multi-language support (English, Luganda, Acholi, Swahili, etc.)
- Farm AI assistant

## Tech Stack

### Core
- **Language**: Kotlin
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)
- **Architecture**: MVVM (Model-View-ViewModel)

### Libraries

#### Networking
- Retrofit 2.9.0 - REST API client
- OkHttp 4.12.0 - HTTP client
- Gson - JSON serialization

#### Architecture Components
- ViewModel & LiveData - Lifecycle-aware data management
- Room Database - Local data persistence
- DataStore - Preferences storage
- Navigation Component - Navigation framework

#### Async
- Kotlin Coroutines - Asynchronous programming
- Kotlin Flow - Reactive streams

#### UI
- Material Design 3 - UI components
- ViewBinding - View access
- ConstraintLayout - Flexible layouts

#### Image Loading
- Coil 2.5.0 - Image loading and caching

#### Firebase
- Firebase Cloud Messaging - Push notifications
- Firebase Analytics - App analytics

#### Google Services
- Google Maps SDK - Location and maps
- Google Places SDK - Location search

#### Charts
- MPAndroidChart - Data visualization

#### Other
- WorkManager - Background tasks
- Image Picker - Camera and gallery integration

## Project Structure

```
app/
├── src/
│   ├── main/
│   │   ├── java/com/agrisonic/app/
│   │   │   ├── data/
│   │   │   │   ├── local/          # Room DB, DataStore
│   │   │   │   │   ├── dao/        # Data Access Objects
│   │   │   │   │   ├── AppDatabase.kt
│   │   │   │   │   ├── PreferencesManager.kt
│   │   │   │   │   └── Converters.kt
│   │   │   │   ├── model/          # Data models
│   │   │   │   │   ├── User.kt
│   │   │   │   │   ├── Auth.kt
│   │   │   │   │   ├── WeatherData.kt
│   │   │   │   │   ├── CropPrediction.kt
│   │   │   │   │   └── MarketPrice.kt
│   │   │   │   └── remote/         # API services
│   │   │   │       ├── api/
│   │   │   │       │   ├── AuthApiService.kt
│   │   │   │       │   ├── WeatherApiService.kt
│   │   │   │       │   ├── CropApiService.kt
│   │   │   │       │   └── MarketApiService.kt
│   │   │   │       └── RetrofitInstance.kt
│   │   │   ├── ui/
│   │   │   │   ├── auth/           # Authentication screens
│   │   │   │   │   ├── LoginActivity.kt
│   │   │   │   │   ├── SignupActivity.kt
│   │   │   │   │   ├── ForgotPasswordActivity.kt
│   │   │   │   │   └── VerifyCodeActivity.kt
│   │   │   │   ├── main/           # Main dashboard
│   │   │   │   │   └── MainActivity.kt
│   │   │   │   ├── splash/         # Splash screen
│   │   │   │   │   └── SplashActivity.kt
│   │   │   │   └── [feature]/      # Feature-specific UIs
│   │   │   ├── service/            # Background services
│   │   │   │   └── AgrisonicFirebaseMessagingService.kt
│   │   │   └── AgrisonicApplication.kt
│   │   ├── res/
│   │   │   ├── layout/             # XML layouts
│   │   │   ├── drawable/           # Images and icons
│   │   │   ├── values/             # Strings, colors, themes
│   │   │   └── xml/                # Config files
│   │   └── AndroidManifest.xml
├── build.gradle                    # App-level build config
└── proguard-rules.pro             # ProGuard rules
```

## API Integration

The app connects to the same backend API as the web version:

**Base URL**: `https://agrisonic.zunobotics.com`

### Key Endpoints

#### Authentication
- `POST /api/auth/signin` - User login
- `POST /api/auth/signup` - User registration
- `POST /api/auth/verify-code` - Email verification
- `GET /api/auth/me` - Get current user
- `POST /api/auth/signout` - Logout

#### Weather
- `GET /api/weather?location={location}&type=forecast&lang={lang}`
- `GET /api/weather?lat={lat}&lon={lon}&type=current`

#### Crop Prediction
- `POST /api/crop-prediction` - Predict suitable crops

#### Market Prices
- `GET /api/market-trends?market={market}&crop={crop}`

## Setup Instructions

### Prerequisites

1. **Android Studio** - Latest stable version (Hedgehog or later)
2. **JDK 17** - Java Development Kit
3. **Android SDK** - API Level 34
4. **Google Maps API Key** - For maps functionality
5. **Firebase Project** - For push notifications

### Configuration

1. **Clone the repository**
   ```bash
   cd agrisonic-app
   ```

2. **Add API Keys**

   Create `local.properties` in the project root:
   ```properties
   sdk.dir=/path/to/Android/sdk
   GOOGLE_MAPS_API_KEY=your_google_maps_api_key_here
   ```

3. **Firebase Configuration**

   - Download `google-services.json` from Firebase Console
   - Place it in `app/` directory

4. **Build Configuration**

   Update API base URL in `app/build.gradle` if needed:
   ```gradle
   buildConfigField "String", "API_BASE_URL", "\"https://agrisonic.zunobotics.com\""
   ```

5. **Sync Gradle**

   Open the project in Android Studio and sync Gradle files.

## Building the App

### Debug Build

```bash
./gradlew assembleDebug
```

Output: `app/build/outputs/apk/debug/app-debug.apk`

### Release Build

```bash
./gradlew assembleRelease
```

Output: `app/build/outputs/apk/release/app-release.apk`

## Running the App

1. **Using Android Studio**
   - Open project in Android Studio
   - Select device/emulator
   - Click Run (▶️)

2. **Using Command Line**
   ```bash
   ./gradlew installDebug
   adb shell am start -n com.agrisonic.app/.ui.splash.SplashActivity
   ```

## Features Implementation Status

### ✅ Completed
- Project structure setup
- Gradle configuration
- Data models (User, Weather, Crop, Market)
- API service interfaces
- Retrofit networking setup
- Room Database setup
- DataStore preferences
- Login activity layout

### 🚧 In Progress
- Authentication UI (Login, Signup, Verify)
- Main dashboard
- Navigation setup

### 📋 Pending
- Weather forecast page
- Crop prediction page
- Market prices page
- Pest detection (camera integration)
- Sensors page
- Chat functionality
- AI Assistant
- Profile management
- Multi-language support
- Offline caching
- Push notifications

## Development Roadmap

### Phase 1: MVP (Minimum Viable Product)
- [x] Project setup
- [x] Data models and API interfaces
- [x] Networking layer
- [ ] Authentication (Login/Signup/Verify)
- [ ] Dashboard home
- [ ] Weather forecast
- [ ] Crop prediction
- [ ] Market prices
- [ ] Basic profile

### Phase 2: Advanced Features
- [ ] Pest detection (AI image analysis)
- [ ] Sensor integration
- [ ] Multi-language translation
- [ ] Offline mode
- [ ] Push notifications

### Phase 3: Communication
- [ ] Group chat
- [ ] Private messaging
- [ ] AI Assistant chatbot

### Phase 4: Polish
- [ ] Performance optimization
- [ ] UI/UX improvements
- [ ] Accessibility features
- [ ] Dark mode
- [ ] App widgets

## Testing

### Run Unit Tests
```bash
./gradlew test
```

### Run Instrumentation Tests
```bash
./gradlew connectedAndroidTest
```

## Code Style

This project follows the [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html) and [Android Kotlin Style Guide](https://developer.android.com/kotlin/style-guide).

## Troubleshooting

### Common Issues

1. **Gradle sync failed**
   - Ensure you have JDK 17 installed
   - Check internet connection
   - Invalidate caches: File → Invalidate Caches / Restart

2. **API calls failing**
   - Check internet connection
   - Verify base URL in BuildConfig
   - Check API server status

3. **Google Maps not showing**
   - Verify API key in `local.properties`
   - Enable Maps SDK in Google Cloud Console
   - Rebuild project

4. **Firebase notifications not working**
   - Ensure `google-services.json` is in `app/` directory
   - Check Firebase console configuration
   - Verify app package name matches Firebase project

## Contributing

When contributing to this project:

1. Create a feature branch
2. Follow Kotlin coding conventions
3. Write unit tests for new features
4. Update documentation
5. Test on multiple devices/API levels
6. Submit pull request

## License

Copyright © 2025 Agrisonic. All rights reserved.

## Contact

For issues or questions:
- Email: [Contact via GitHub Issues]
- GitHub Issues: [Create an issue](https://github.com/ZunoBotics/Agrisonic-App/issues)

---

Built with ❤️ for Ugandan farmers
