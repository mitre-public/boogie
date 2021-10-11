#!/bin/bash
set -e

NONE="$(echo -e "\033[0m")"
RED="$(echo -e "\033[31m")"
GREEN="$(echo -e "\033[32m")"
YELLOW="$(echo -e "\033[33m")"
BLUE="$(echo -e "\033[34m")"

# Unlike the internal deploy there is no TDP namespace on codev right now (which is what we're targeting) so we're
# using a sub-space under IDAASS for TDP-related artifacts
IMAGE_NAME=${external_artifactory}/${external_artifactory_namespace}/${external_artifactory_prefix}/boogie-rest
echo "Image Name: $IMAGE_NAME"

BOOGIE_VERSION=$(./gradlew properties --no-daemon --console=plain -q | grep "^version:" | awk '{printf $2}')
echo "Boogie Version: $BOOGIE_VERSION"

CURRENT_COMMIT=$(git rev-parse --short HEAD)
echo "Current Commit: $CURRENT_COMMIT"

IMAGE_VERSION=${BOOGIE_VERSION}-${CURRENT_COMMIT}
echo "Image Version: $IMAGE_VERSION"

docker build . -t ${IMAGE_NAME}:${IMAGE_VERSION} --format docker

# This needs to be updated to check against the templated namespace (instead of hardcoded as of right now)
function check_if_image_tag_exists() {
    curl -sSf -u ${external_artifactory_user}:${external_artifactory_password} https://artifacts.mitre.org:443/artifactory/api/storage/docker/tdp/boogie-rest/$1/ > /dev/null 2>&1
}

if [ ${bamboo_repository_branch_name} = "main" ]; then
	echo "${GREEN}publishing docker image...$NONE"
	docker login -u ${external_artifactory_user} -p ${external_artifactory_password} ${external_artifactory}
  if check_if_image_tag_exists ${IMAGE_VERSION}; then
    echo "Image - ${IMAGE_NAME}:${IMAGE_VERSION} already exists"
  else
    echo "Pushing Image - ${IMAGE_NAME}:${IMAGE_VERSION}"
    docker push ${IMAGE_NAME}:${IMAGE_VERSION}
  fi
else
	echo "not on ${YELLOW}main${NONE} (${BLUE}$bamboo_repository_branch_name${NONE}) branch... ${RED}NOT PUBLISHING!${NONE}"
fi