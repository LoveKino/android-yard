#!/bin/bash

CUR_DIR=$(cd `dirname $0`;pwd)

cd $CUR_DIR;

rm -rf target && mkdir target

# compile source code
javac -source 1.7 -target 1.7 src/com/freekite/android/**/**.java -d target

# compile dex jar
pushd target
jar cvf yard.jar com

$HOME/Library/Android/sdk/build-tools/24.0.2/dx --dex --output=classes.dex yard.jar

jar cvf yard-dex.jar classes.dex

# publish to machine
adb push yard-dex.jar /storage/emulated/0/aosp_hook
popd
