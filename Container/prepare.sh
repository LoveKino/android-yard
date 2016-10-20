#!/bin/bash

CUR_DIR=$(cd `dirname $0`;pwd);

TEST_FILE_DIR=/data/user/0/com.freekite.android.container

cd $CUR_DIR

# copy messchunkpc project

cp -R ../Adbcon/messchunkpc/src/main/java/com ./yard/src/main/java

# deploy yard-dex.jar

../bin/deploy.sh $TEST_FILE_DIR

# update library

cp -R ../node_modules/expand-android-api/AndroidContainer/aospPatch aospPatch
