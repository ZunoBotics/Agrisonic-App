# Quick Start Guide - Agrisonic Android App

## 🚀 Get Started in 5 Minutes

### Step 1: Open the Project

```bash
cd /Users/jonathan/Agrisonic/agrisonic-app
```

Then open this folder in **Android Studio**.

### Step 2: Configure API Keys

Create a file named `local.properties` in the project root:

```properties
sdk.dir=/Users/jonathan/Library/Android/sdk
GOOGLE_MAPS_API_KEY=YOUR_GOOGLE_MAPS_API_KEY_HERE
```

*(Replace with your actual Android SDK path and Google Maps API key)*

**To get a Google Maps API Key:**
1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create/select a project
3. Enable "Maps SDK for Android"
4. Create credentials → API Key
5. Add the key to `local.properties`

### Step 3: Add Firebase Configuration

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Select your `agrisonic-notifications` project
3. Add an Android app with package name: `com.agrisonic.app`
4. Download `google-services.json`
5. Place it in `agrisonic-app/app/` directory

**OR** Create a placeholder file for now:

```bash
cat > app/google-services.json << 'EOF'
{
  "project_info": {
    "project_number": "309674815785",
    "project_id": "agrisonic-notifications",
    "storage_bucket": "agrisonic-notifications.firebasestorage.app"
  },
  "client": [
    {
      "client_info": {
        "mobilesdk_app_id": "1:309674815785:android:abc123",
        "android_client_info": {
          "package_name": "com.agrisonic.app"
        }
      }
    }
  ],
  "configuration_version": "1"
}
EOF
```

### Step 4: Sync Gradle

In Android Studio:
1. File → Sync Project with Gradle Files
2. Wait for dependencies to download

### Step 5: Build & Run

1. Connect an Android device or start an emulator
2. Click the **Run** button (▶️) or press `Shift + F10`
3. Select your device
4. Wait for the app to install and launch

## ✅ What's Working Now

- ✅ Project builds successfully
- ✅ Login screen UI is ready
- ✅ API integration is configured
- ✅ Database is set up
- ✅ Session management works

## 🚧 What Needs to be Built

### Required for MVP:

1. **Complete Authentication Flow**
   - Create `SignupActivity.kt`
   - Create `VerifyCodeActivity.kt`
   - Create `ForgotPasswordActivity.kt`
   - Create `SplashActivity.kt`

2. **Main Dashboard**
   - Create `MainActivity.kt`
   - Add bottom navigation
   - Create home screen layout

3. **Core Features**
   - Weather forecast page
   - Crop prediction page
   - Market prices page

## 📝 Create Your First Activity

Let's build the Splash Screen as an example:

### 1. Create Activity File

`app/src/main/java/com/agrisonic/app/ui/splash/SplashActivity.kt`:

```kotlin
package com.agrisonic.app.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.agrisonic.app.AgrisonicApplication
import com.agrisonic.app.ui.auth.LoginActivity
import com.agrisonic.app.ui.main.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    private val preferencesManager by lazy {
        (application as AgrisonicApplication).preferencesManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            delay(2000) // Show splash for 2 seconds

            val isLoggedIn = preferencesManager.isLoggedIn().first()

            val intent = if (isLoggedIn) {
                Intent(this@SplashActivity, MainActivity::class.java)
            } else {
                Intent(this@SplashActivity, LoginActivity::class.java)
            }

            startActivity(intent)
            finish()
        }
    }
}
```

### 2. Create Layout (Optional - using theme)

The splash screen uses the theme defined in `themes.xml`, so no layout needed!

### 3. That's it!

The splash screen is complete. Now build similar activities for:
- Signup
- Forgot Password
- Verify Code
- Main Dashboard

## 🎯 Next Priority Tasks

1. **Signup Activity** - Similar to Login, but with name field and confirm password
2. **Verify Code Activity** - 6-digit code input (use OTP view)
3. **Main Activity** - Bottom navigation with fragments
4. **Weather Fragment** - Show weather forecast
5. **Crop Prediction Fragment** - Input soil parameters
6. **Market Prices Fragment** - List market prices

## 🔧 Useful Android Studio Shortcuts

- `Cmd + O` (Mac) / `Ctrl + O` (Win) - Open class
- `Cmd + Shift + O` - Open file
- `Cmd + B` - Go to declaration
- `Cmd + /` - Comment/uncomment line
- `Ctrl + Space` - Code completion
- `Cmd + Shift + F` - Find in path

## 📚 Resources

### Android Documentation
- [Kotlin Android Guide](https://developer.android.com/kotlin)
- [Material Design](https://material.io/develop/android)
- [Retrofit Guide](https://square.github.io/retrofit/)
- [Room Database](https://developer.android.com/training/data-storage/room)

### Agrisonic Backend API
- Base URL: `https://agrisonic.zunobotics.com`
- API Docs: Check `agrisonic/app/api/` directory in React app

## 🐛 Troubleshooting

### Issue: Gradle sync failed
**Solution**:
- Ensure JDK 17 is installed
- File → Invalidate Caches / Restart

### Issue: Cannot resolve symbol 'R'
**Solution**:
- Build → Clean Project
- Build → Rebuild Project

### Issue: API calls not working
**Solution**:
- Check internet connection
- Verify `BuildConfig.API_BASE_URL`
- Check server is running

### Issue: App crashes on login
**Solution**:
- Check Logcat for error messages
- Verify API endpoint is correct
- Test with Postman first

## 💬 Need Help?

1. Check [README.md](README.md) for detailed docs
2. Read [PROJECT_SUMMARY.md](PROJECT_SUMMARY.md) for overview
3. Review React app in `../agrisonic/` for API examples
4. Check Android Studio Logcat for errors

## ✨ Pro Tips

1. **Use Live Templates**: Type `fbc` → Enter for findViewById
2. **Extract Resources**: Alt + Enter on hardcoded string → Extract string resource
3. **Format Code**: Cmd + Alt + L (Mac) / Ctrl + Alt + L (Win)
4. **Organize Imports**: Cmd + Alt + O (Mac) / Ctrl + Alt + O (Win)
5. **Run with Debug**: Shift + F9 for debugging

---

**You're all set!** 🎉

Open the project in Android Studio and start building. The foundation is solid - now add the features page by page!
