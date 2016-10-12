#!/bin/bash

# eg: com.android.freekite.patch.aosppatch com.android.freekite.patch.aosppatch.MainActivity

APP_PKG=$1
APP_MAIN_ACTIVITY=$2

adb shell am force-stop $APP_PKG
adb shell am start -n $APP_PKG/$APP_MAIN_ACTIVITY
