#!/bin/bash

# Quick run script for Agrisonic Android App
# Usage: ./run.sh [device_id]

ADB="$HOME/Library/Android/sdk/platform-tools/adb"
DEVICE="${1:-R5CX20AMHHH}"  # Default to physical device

echo "📱 Using device: $DEVICE"
echo ""

# Build
echo "🔨 Building..."
./gradlew assembleDebug -q

# Install
echo "📦 Installing..."
$ADB -s $DEVICE install -r app/build/outputs/apk/debug/app-debug.apk

# Launch
echo "🚀 Launching..."
$ADB -s $DEVICE shell am start -n com.agrisonic.app/.ui.splash.SplashActivity

echo ""
echo "✅ Done! Check your device."
echo ""
echo "📊 View logs: $ADB -s $DEVICE logcat | grep Agrisonic"
