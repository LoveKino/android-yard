#!/bin/bash

LIBRARY_NAME=$1
LIBRARY_PATH=$2
PROJECT_PATH=$3

rm -rf $TEST_DIR/$LIBRARY_NAME
cp -R $LIBRARY_PATH $PROJECT_PATH