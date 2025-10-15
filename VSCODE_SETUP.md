# Using VS Code for Android Development

## ⚠️ Important Note

While you **can** use VS Code for Android development, **Android Studio is highly recommended** for:
- Visual layout editor
- Better auto-completion
- Built-in emulator
- Gradle integration
- Debugging tools

However, if you prefer VS Code, here's how to set it up:

## Prerequisites

1. **Install Android SDK**
   ```bash
   # Download Android Command Line Tools from:
   # https://developer.android.com/studio#command-tools

   # Extract to ~/Android/sdk
   mkdir -p ~/Library/Android/sdk
   cd ~/Library/Android/sdk
   # Unzip command-line tools here
   ```

2. **Set Environment Variables**

   Add to `~/.zshrc` or `~/.bash_profile`:
   ```bash
   export ANDROID_HOME=$HOME/Library/Android/sdk
   export PATH=$PATH:$ANDROID_HOME/emulator
   export PATH=$PATH:$ANDROID_HOME/platform-tools
   export PATH=$PATH:$ANDROID_HOME/cmdline-tools/latest/bin

   # Java (use Homebrew)
   export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home
   ```

3. **Install Java 17**
   ```bash
   brew install openjdk@17
   ```

## VS Code Extensions

Install these extensions:

1. **Kotlin Language** by mathiasfrohlich
   - Syntax highlighting
   - Code completion
   - Formatting

2. **Android iOS Emulator** by DiemasMichiels
   - Run emulator from VS Code

3. **Gradle for Java** by Microsoft
   - Gradle task support

4. **XML Tools** by Josh Johnson
   - XML formatting

5. **Error Lens** by Alexander
   - Inline error display

Install via terminal:
```bash
code --install-extension mathiasfrohlich.Kotlin
code --install-extension DiemasMichiels.emulate
code --install-extension vscjava.vscode-gradle
code --install-extension redhat.vscode-xml
code --install-extension usernamehw.errorlens
```

## Building the Project in VS Code

### 1. Open the Project
```bash
cd /Users/jonathan/Agrisonic/agrisonic-app
code .
```

### 2. Sync Gradle (First Time)
```bash
./gradlew build --refresh-dependencies
```

### 3. Build Debug APK
```bash
./gradlew assembleDebug
```

Output: `app/build/outputs/apk/debug/app-debug.apk`

### 4. Build Release APK
```bash
./gradlew assembleRelease
```

## Running the App

### Option 1: Using Android Emulator

1. **Create an AVD (Android Virtual Device)**
   ```bash
   # List available system images
   sdkmanager --list | grep system-images

   # Install a system image
   sdkmanager "system-images;android-34;google_apis;arm64-v8a"

   # Create AVD
   avdmanager create avd -n Pixel_7 -k "system-images;android-34;google_apis;arm64-v8a" -d pixel_7

   # Start emulator
   emulator -avd Pixel_7
   ```

2. **Install APK**
   ```bash
   ./gradlew installDebug

   # Or manually
   adb install app/build/outputs/apk/debug/app-debug.apk
   ```

3. **Launch App**
   ```bash
   adb shell am start -n com.agrisonic.app/.ui.splash.SplashActivity
   ```

### Option 2: Using Physical Device

1. **Enable USB Debugging** on your Android device
   - Settings → About Phone → Tap "Build Number" 7 times
   - Settings → Developer Options → Enable USB Debugging

2. **Connect Device**
   ```bash
   # Check device is connected
   adb devices
   ```

3. **Install and Run**
   ```bash
   ./gradlew installDebug
   adb shell am start -n com.agrisonic.app/.ui.splash.SplashActivity
   ```

## VS Code Tasks

Create `.vscode/tasks.json`:

```json
{
  "version": "2.0.0",
  "tasks": [
    {
      "label": "Build Debug",
      "type": "shell",
      "command": "./gradlew assembleDebug",
      "group": {
        "kind": "build",
        "isDefault": true
      }
    },
    {
      "label": "Install Debug",
      "type": "shell",
      "command": "./gradlew installDebug"
    },
    {
      "label": "Clean Build",
      "type": "shell",
      "command": "./gradlew clean build"
    },
    {
      "label": "Run on Device",
      "type": "shell",
      "command": "adb shell am start -n com.agrisonic.app/.ui.splash.SplashActivity",
      "dependsOn": ["Install Debug"]
    }
  ]
}
```

Run with: `Cmd + Shift + B`

## Useful Commands

```bash
# Clean project
./gradlew clean

# Build debug APK
./gradlew assembleDebug

# Install on connected device
./gradlew installDebug

# Uninstall from device
adb uninstall com.agrisonic.app

# View logs
adb logcat | grep Agrisonic

# Clear app data
adb shell pm clear com.agrisonic.app

# Take screenshot
adb exec-out screencap -p > screenshot.png

# List installed packages
adb shell pm list packages | grep agrisonic
```

## Debugging in VS Code

While VS Code doesn't have native Android debugging, you can:

1. **View Logs**
   ```bash
   # Filter by app
   adb logcat | grep com.agrisonic.app

   # Filter by tag
   adb logcat | grep "LoginActivity"

   # Clear logs
   adb logcat -c
   ```

2. **Use Print Statements**
   ```kotlin
   import android.util.Log

   Log.d("TAG", "Debug message")
   Log.e("TAG", "Error message")
   Log.i("TAG", "Info message")
   ```

3. **Crash Reports**
   ```bash
   # View crash logs
   adb logcat | grep AndroidRuntime
   ```

## Layout Editing

In VS Code, you'll edit XML layouts as text:

```xml
<!-- app/src/main/res/layout/activity_main.xml -->
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    <TextView
        android:id="@+id/tvTitle"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Hello World" />

</LinearLayout>
```

**Tip**: Use Android's [Material Design Guidelines](https://material.io/design) and copy XML from documentation.

## Limitations of VS Code

1. ❌ No visual layout preview
2. ❌ No drag-and-drop UI builder
3. ❌ No resource management UI
4. ❌ No integrated emulator controls
5. ❌ Limited auto-completion for Android APIs
6. ❌ No APK analyzer
7. ❌ No profiler

## Recommended Hybrid Approach

**Use both tools:**

1. **VS Code** for:
   - Writing Kotlin business logic
   - Editing simple XML files
   - Git operations
   - Quick edits

2. **Android Studio** for:
   - Creating new layouts
   - Visual UI design
   - Debugging complex issues
   - Running emulator
   - Building release APKs

## Quick Command Reference

```bash
# Development cycle
./gradlew clean                    # Clean
./gradlew assembleDebug           # Build
./gradlew installDebug            # Install
adb shell am start -n com.agrisonic.app/.ui.splash.SplashActivity  # Run

# One-liner
./gradlew installDebug && adb shell am start -n com.agrisonic.app/.ui.splash.SplashActivity

# View logs while running
adb logcat | grep -E "Agrisonic|AndroidRuntime"
```

## My Recommendation

For this project, I **strongly recommend using Android Studio** because:

1. ✅ This is your first Android app
2. ✅ You'll need the visual layout editor
3. ✅ Better error messages and fixes
4. ✅ Integrated emulator is much easier
5. ✅ Better Kotlin support and auto-completion
6. ✅ One-click build and run

**However**, if you're comfortable with command-line tools and prefer VS Code's lightweight feel, the setup above will work!

---

**Bottom Line**: You *can* use VS Code, but you'll have a much smoother experience with Android Studio, especially for UI work.
