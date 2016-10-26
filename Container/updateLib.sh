
#!/bin/bash

CUR_DIR=$(cd `dirname $0`;pwd);

cd $CUR_DIR

# copy messchunkpc project

cp -R ../Adbcon/messchunkpc/src/main/java/com ./yard/src/main/java

# update library

cp -R ../node_modules/expand-android-api/AndroidContainer/aospPatch aospPatch
