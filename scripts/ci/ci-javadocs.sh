#!/bin/bash

set -e

SCRIPT_DIR="$(dirname "$(realpath "$0")")"
source $SCRIPT_DIR/ci-script-colors.sh

branchName=$(git rev-parse --abbrev-ref HEAD)
myProjVersion=$(./gradlew properties -q | grep "version:" | awk '{print $2}' | tr -d '[:space:]')
echo "Running ${GREEN}$0${NONE} on ${GREEN}$branchName${NONE} at ${GREEN}$myProjVersion${NONE}"

# create, zip javadocs
./gradlew javadocPackage