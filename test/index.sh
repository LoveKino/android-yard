#!/bin/bash

CUR_DIR=$(cd `dirname $0`;pwd);

cd $CUR_DIR;

# run testApp1's tests
./testApp1.sh

../node_modules/.bin/mocha -t 100000 node/index.js
