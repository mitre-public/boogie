#!/bin/bash
# This script releases the new stable CDA artifacts. You need to provide the maven credentials:
#
# ./ci-prepare-release.sh -PmavenUser=username -PmavenPassword=pass
#
# Tthis script will:
# 1. Ensure you're on a clean working copy of git
# 2. Bump the version to a stable artifact (i.e., remove "-SNAPSHOT")
# 3. Commit and tag the release
# 4. Bump to the next snapshot version (i.e., increment patch and add "-SNAPSHOT")
# 5. Push changes to master
# 6. Publish artifacts to Nexus from the stable commit
#
# See: https://github.com/researchgate/gradle-release for details on the Gradle release plugin

echo "Running CI Script 'ci-prepare-release' using CI branch: $bamboo_planRepository_branchName"
myProjVersion=$(./gradlew properties -q | grep "version:" | awk '{print $2}' | tr -d '[:space:]')

echo "current dev version: $myProjVersion"

# bump to stable, commit+tag+push; then bump to next snapshot, commit+push
./gradlew release -Prelease.useAutomaticVersion=true

# if the above was successful, back up to stable version's commit & deploy to dali
if [ $? -eq 0 ]
then
  echo "Successfully created version bump commits... deploying to Dali"
  # back up to stable commit
  git checkout HEAD^
  ./gradlew publish $1 $2
else
  echo "Failure: unable to make version commits" >&2
  exit 1
fi
