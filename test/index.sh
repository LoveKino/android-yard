#!/bin/bash

CUR_DIR=$(cd `dirname $0`;pwd);

cd $CUR_DIR;

./buildTestApp.sh

# run test
echo "[run app tests]--------------------------------"
pushd TestApp1
./gradlew cAT
popd

./testServer.sh
