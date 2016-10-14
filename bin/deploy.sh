#!/bin/bash

# eg: deployYard.sh /data/user/0/com.android.freekite.patch.aosppatch

CUR_DIR=$(cd `dirname $0`;pwd);

APP_DATA_DIR=$1

cd $CUR_DIR

./build.sh

adb root

../node_modules/.bin/build-android-patch-deployYard $APP_DATA_DIR ../target/yard-dex.jar
