#!/bin/bash

# test cmd: ./deployYard.sh /data/user/0/com.android.freekite.patch.aosppatch /Users/yuer/workspaceforme/category/career/container/app/freekite/app/business/android-yard/target/yard-dex.jar 

APP_PATH=$1
YARD_PATH=$2
INNER_FILES=$APP_PATH/files
HOOK_DIR=$INNER_FILES/aosp_hook
YARD_FILE_NAME=$(basename $YARD_PATH)
HOOK_YARD_FILE_PATH=$HOOK_DIR/$YARD_FILE_NAME

# check params

[ -z $APP_PATH ] && echo "[Error] missing app path" && exit 1
[ -z $YARD_PATH ] && echo "[Error] missing yard file path" && exit 1

# mkdir aosp_hook in files dir
adb shell rm -r $HOOK_DIR

adb shell mkdir -p $HOOK_DIR

# push

adb push $YARD_PATH $HOOK_DIR

# change context security
CON_SEC=$(adb shell ls -dZ $APP_PATH | cut -d " " -f1)
echo "app security context: "$CON_SEC

adb shell chcon $CON_SEC $INNER_FILES
adb shell chcon $CON_SEC $HOOK_DIR

# change owner and group
USER_NAME=$(adb shell stat -c "%U" $APP_PATH)
GROUP_NAME=$(adb shell stat -c "%G" $APP_PATH)

echo "app user and group: $USER_NAME $GROUP_NAME"

adb shell chown $USER_NAME $INNER_FILES
adb shell chgrp $GROUP_NAME $INNER_FILES

adb shell chown $USER_NAME $HOOK_DIR
adb shell chgrp $GROUP_NAME $HOOK_DIR

adb shell chown $USER_NAME $HOOK_YARD_FILE_PATH
adb shell chgrp $GROUP_NAME $HOOK_YARD_FILE_PATH
