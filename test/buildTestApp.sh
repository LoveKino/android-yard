#!/bin/bash

CUR_DIR=$(cd `dirname $0`;pwd);

cd $CUR_DIR;

# update library
rm -rf ./TestApp1/aospPatch
cp -R ../node_modules/expand-android-api/AndroidContainer/aospPatch ./TestApp1

# build test project
pushd TestApp1
./gradlew build
popd

# uninstall
adb uninstall com.freekite.android.yard.testapp1
adb install ./TestApp1/app/build/outputs/apk/app-debug.apk

../bin/deploy.sh /data/user/0/com.freekite.android.yard.testapp1
