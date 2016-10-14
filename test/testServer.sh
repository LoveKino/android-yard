#!/bin/bash

CUR_DIR=$(cd `dirname $0`;pwd);

cd $CUR_DIR

# build
./buildTestApp.sh

# run node tests
../node_modules/.bin/mocha -t 10000 node/**/*.js
