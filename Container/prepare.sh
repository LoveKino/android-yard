#!/bin/bash

CUR_DIR=$(cd `dirname $0`;pwd);

TEST_FILE_DIR=/data/user/0/com.freekite.android.container

cd $CUR_DIR

./updateLib.sh

# deploy yard-dex.jar

../bin/deploy.sh $TEST_FILE_DIR
