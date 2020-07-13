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

# run clean set of tests with jacoco reports configured
./gradlew clean test -Preporting --no-build-cache

## generate code coverage aggregate report (explictly depends on testSmall+testLarge)
./gradlew codeCoverageReport -Preporting --no-build-cache --no-parallel

# run  sonarqube scanner, which by default depends on the 'test' task
# (we already ran the tests we want so we skip tests here with `-x test`)
# see: https://docs.sonarqube.org/latest/analysis/scan/sonarscanner-for-gradle/
./gradlew sonarqube -Preporting -x test
