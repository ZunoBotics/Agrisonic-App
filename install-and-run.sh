#!/bin/bash

# Install and Run Agrisonic Android App
# This script builds, installs, and launches the app

ADB="$HOME/Library/Android/sdk/platform-tools/adb"

echo "🔨 Building APK..."
./gradlew assembleDebug

if [ $? -eq 0 ]; then
    echo "✅ Build successful!"

    echo ""
    echo "📱 Checking for connected devices..."
    $ADB devices -l

    echo ""
    echo "📦 Installing app..."
    $ADB install -r app/build/outputs/apk/debug/app-debug.apk

    if [ $? -eq 0 ]; then
        echo "✅ App installed!"

        echo ""
        echo "🚀 Launching app..."
        $ADB shell am start -n com.agrisonic.app/.ui.splash.SplashActivity

        echo ""
        echo "✅ App launched! Check your device."
        echo ""
        echo "📊 View logs:"
        echo "  $ADB logcat | grep Agrisonic"
    else
        echo "❌ Installation failed!"
        echo ""
        echo "💡 Make sure:"
        echo "  1. Your device is connected via USB"
        echo "  2. USB debugging is enabled"
        echo "  3. You've authorized this computer on your device"
    fi
else
    echo "❌ Build failed!"
fi
