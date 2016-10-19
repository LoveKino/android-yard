#!/bin/bash

CUR_DIR=$(cd `dirname $0`;pwd);

TEST_DIR=$1
TEST_PKG_NAME=$2
TEST_FILE_DIR=$3

cd $CUR_DIR;

# build test project
pushd $TEST_DIR
./gradlew build
popd

# uninstall
adb uninstall $TEST_PKG_NAME
adb install $TEST_DIR/app/build/outputs/apk/app-debug.apk

../bin/deploy.sh $TEST_FILE_DIR

cd $TEST_DIR
./gradlew cAT
[ $? -ne 0 ] && exit $?
