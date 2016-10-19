#!/bin/bash

CUR_DIR=$(cd `dirname $0`;pwd);

cd $CUR_DIR;

./testApp1.sh

../node_modules/.bin/mocha -t 100000 node/**/*.js
