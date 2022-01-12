#!/bin/bash

NONE="$(echo -e "\033[0m")"
RED="$(echo -e "\033[31m")"
GREEN="$(echo -e "\033[32m")"
YELLOW="$(echo -e "\033[33m")"
BLUE="$(echo -e "\033[34m")"

branchName=$(git rev-parse --abbrev-ref HEAD)
myProjVersion=$(./gradlew properties -q | grep "version:" | awk '{print $2}' | tr -d '[:space:]')
echo "Running ${BLUE}$0${NONE} on branch ${BLUE}$branchName${NONE} at version: ${BLUE}$myProjVersion${NONE}"

# only publish for this build if SNAPSHOT version AND on main branch
if [[ "$myProjVersion" == *SNAPSHOT ]] && [ $branchName = "main" ]; then
  echo "${GREEN}publishing a main SNAPSHOT...$NONE"
  output=$(./gradlew publish -PmavenUser=$1 -PmavenPassword=$2)
  echo "$output"
else
  echo "not on ${YELLOW}main${NONE} (${BLUE}$branchName${NONE}) branch or not on a ${YELLOW}SNAPSHOT${NONE} (${BLUE}$myProjVersion${NONE}) version.. ${RED}NOT PUBLISHING!${NONE}"
fi
