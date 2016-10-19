#!/bin/bash

CUR_DIR=$(cd `dirname $0`;pwd);

TEST_DIR=./TestApp1
TEST_PKG_NAME=com.freekite.android.yard.testapp1
TEST_FILE_DIR=/data/user/0/com.freekite.android.yard.testapp1

cd $CUR_DIR;

./updateLibrary.sh aospPatch ../node_modules/expand-android-api/AndroidContainer/aospPatch $TEST_DIR

# build test project
pushd $TEST_DIR
./gradlew build
popd

# uninstall
adb uninstall $TEST_PKG_NAME
adb install $TEST_DIR/app/build/outputs/apk/app-debug.apk

../bin/deploy.sh $TEST_FILE_DIR

# run tests
pushd $TEST_DIR
./gradlew cAT
./buildTestApp.sh $TEST_DIR $TEST_PKG_NAME $TEST_FILE_DIR
popd
