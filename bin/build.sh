#!/bin/bash

CUR_DIR=$(cd `dirname $0`;pwd)

cd $CUR_DIR;

pushd ../Container;

./gradlew buildDependents

popd

cd ..

rm -rf target && mkdir target

# compile dex jar
pushd target

cp ../Container/yard/build/outputs/aar/yard-release.aar ./

unzip yard-release.aar -d tmp

$HOME/Library/Android/sdk/build-tools/24.0.2/dx --dex --output=classes.dex tmp/classes.jar

#
jar cvf yard-dex.jar classes.dex

popd
