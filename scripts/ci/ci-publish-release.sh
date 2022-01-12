#!/bin/bash
# This script releases the new stable Boogie artifacts. You need to provide the maven credentials:
#
# ./scripts/ci/ci-publish-release.sh -PmavenUser=username -PmavenPassword=pass
# (or have mavenUser/mavenPassword set in your global gradle.properties, typically in you ~/.gradle folder)
#
# This script will:
# 1. Ensure you're on a clean working copy of git
# 2. Bump the version to a stable artifact (i.e., remove "-SNAPSHOT")
# 3. Commit and tag the release
# 4. Bump to the next snapshot version (i.e., increment patch and add "-SNAPSHOT")
# 5. Push changes to main
# 6. Publish artifacts to Nexus from the stable commit
#
# See: https://github.com/researchgate/gradle-release for details on the Gradle release plugin
#
# Note that this script is also configured to be runnable from CI via an optional task, you can see the ./bamboo-specs CI plan
# spec for additional information

NONE="$(echo -e "\033[0m")"
RED="$(echo -e "\033[31m")"
GREEN="$(echo -e "\033[32m")"
YELLOW="$(echo -e "\033[33m")"
BLUE="$(echo -e "\033[34m")"

if [ $bamboo_planRepository_branchName != "main" ] ; then
  echo "Not on main - skipping release"
  exit 1
fi

echo "Shallow cloning source repo..."

git remote set-url origin "https://${bamboo_git_username}:${bamboo_git_tokenpassword}@mustache.mitre.org/scm/ttfs/boogie.git"
git remote -v

cd boogie

# Service account login
git config user.name "${bamboo_git_username}"
git config user.email "${bamboo_git_email}"

branchName=$(git rev-parse --abbrev-ref HEAD)
myProjVersion=$(./gradlew properties -q | grep "version:" | awk '{print $2}' | tr -d '[:space:]')
echo "Running ${BLUE}$0${NONE} on branch ${BLUE}$branchName${NONE} at version: ${BLUE}$myProjVersion${NONE}"

echo "current dev version: $myProjVersion"

# bump to stable, commit+tag+push; then bump to next snapshot, commit+push
./gradlew release -Prelease.useAutomaticVersion=true

# if the above was successful, back up to stable version's commit & deploy to dali
if [ $? -eq 0 ]; then
  echo "Successfully created version bump commits... deploying to Dali"
  # back up to stable commit
  git checkout HEAD^
  ./gradlew publish -PmavenUser=$1 -PmavenPassword=$2
else
  echo "Failure: unable to make version commits" >&2
  exit 1
fi
