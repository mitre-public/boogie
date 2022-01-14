#!/bin/bash

set -e

NONE="$(echo -e "\033[0m")"
RED="$(echo -e "\033[31m")"
GREEN="$(echo -e "\033[32m")"
YELLOW="$(echo -e "\033[33m")"
BLUE="$(echo -e "\033[34m")"

branchName=$(git rev-parse --abbrev-ref HEAD)
myProjVersion=$(./gradlew properties -q | grep "version:" | awk '{print $2}' | tr -d '[:space:]')
echo "Running ${GREEN}$0${NONE} on ${GREEN}$branchName${NONE} at ${GREEN}$myProjVersion${NONE}"

# create, zip javadocs
./gradlew javadocPackage