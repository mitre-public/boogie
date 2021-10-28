#!/bin/bash
set -e

NONE="$(echo -e "\033[0m")"
RED="$(echo -e "\033[31m")"
GREEN="$(echo -e "\033[32m")"
YELLOW="$(echo -e "\033[33m")"
BLUE="$(echo -e "\033[34m")"

# Unlike the internal deploy there is no TDP namespace on codev right now (which is what we're targeting) so we're
# using a sub-space under IDAASS for TDP-related artifacts
IMAGE_NAME=${bamboo_external_artifactory}/${bamboo_external_artifactory_namespace}/${bamboo_external_artifactory_prefix}/boogie-rest
echo "Image Name: $IMAGE_NAME"

BOOGIE_VERSION=$(./gradlew properties --no-daemon --console=plain -q | grep "^version:" | awk '{printf $2}')
echo "Boogie Version: $BOOGIE_VERSION"

CURRENT_COMMIT=$(git rev-parse --short HEAD)
echo "Current Commit: $CURRENT_COMMIT"

IMAGE_VERSION=${BOOGIE_VERSION}-${CURRENT_COMMIT}
echo "Image Version: $IMAGE_VERSION"

docker build . -t ${IMAGE_NAME}:${IMAGE_VERSION} --format docker

# hit the remote artifactory rest API to see if an image in the correct namespace with a matching tag already exists
function check_if_image_tag_exists() {
  REST_URL="https://repo.codev.mitre.org/${bamboo_external_artifactory_namespace}/${bamboo_external_artifactory_prefix}/boogie-rest/$1/"
  curl -sSf -u ${bamboo_external_artifactory_user}:${bamboo_external_artifactory_password} ${REST_URL} > /dev/null 2>&1
}

if [ ${bamboo_repository_branch_name} = "main" ]; then
	echo "${GREEN}publishing docker image...$NONE"
	docker login -u ${bamboo_external_artifactory_user} -p ${bamboo_external_artifactory_password} ${bamboo_external_artifactory}
  if check_if_image_tag_exists ${IMAGE_VERSION}; then
    echo "Image - ${IMAGE_NAME}:${IMAGE_VERSION} already exists"
  else
    echo "Pushing Image - ${IMAGE_NAME}:${IMAGE_VERSION}"
    docker push ${IMAGE_NAME}:${IMAGE_VERSION}
  fi
else
	echo "not on ${YELLOW}main${NONE} (${BLUE}$bamboo_repository_branch_name${NONE}) branch... ${RED}NOT PUBLISHING!${NONE}"
fi