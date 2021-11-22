#!/bin/bash
set -e

# This script exists to allow users to specify a collection of parameters and then publish a tagged version of the Boogie docker
# image to whatever target image repository and namespace they specified (and with whatever version tag they want).
#
# This script takes 4 or 5 total arguments:
#
# 1. The url to the artifactory or image repository location you want to publish the image to (e.g. repo.codev.mitre.org)
# 2. The user to publish as which is typically a service account (e.g. svc-idaass-developers)
# 3. The secret for that user to authenticate with the server
# 4. The name of the image and the namespace it should be in (e.g. idaass-docker/tdp/boogie-rest)
#
# 5. An optional image version to publish with (e.g. latest, etc.) - if one isn't provided the current software version and commit
# from the repo will be used to create one (e.g. 1.0.3-release-aabbcc)

NONE="$(echo -e "\033[0m")"
RED="$(echo -e "\033[31m")"
GREEN="$(echo -e "\033[32m")"
YELLOW="$(echo -e "\033[33m")"
BLUE="$(echo -e "\033[34m")"

# Named set of inputs to run the script
ARTIFACTORY=$1
USER=$2
SECRET=$3
IMAGE=$4

# if a version is provided use that in the deployment - otherwise create what should be a generally unique one via the software
# version and current commit
if [ "$#" = "5" ]; then
  echo "Using provided image tag in deployment: $5"
  IMAGE_VERSION=$5
else
  TTFS_VERSION=$(./gradlew properties --no-daemon --console=plain -q | grep "^version:" | awk '{printf $2}')
  echo "TTFS Version: $TTFS_VERSION"

  CURRENT_COMMIT=$(git rev-parse --short HEAD)
  echo "Current Commit: $CURRENT_COMMIT"

  BASE_VERSION=${TTFS_VERSION}-${CURRENT_COMMIT}

  # drop "snapshot" from the image name, just keep the version number and tag it as a specific release...
  IMAGE_VERSION="${BASE_VERSION/SNAPSHOT/release}"
  echo "Image Version: $IMAGE_VERSION"
fi


# Unlike the internal deploy there is no TDP namespace on codev right now (which is what we're targeting) so we're
# using a sub-space under IDAASS for TDP-related artifacts
IMAGE_NAME=$ARTIFACTORY/$IMAGE
echo "Image Name: $IMAGE_NAME"

docker build . -t ${IMAGE_NAME}:${IMAGE_VERSION} --format docker

# hit the remote artifactory rest API to see if an image in the correct namespace with a matching tag already exists
function check_if_image_tag_exists() {
  REST_URL="https://$ARTIFACTORY/$IMAGE_NAME/$1/"
  curl -sSf -u $USER:SECRET ${REST_URL} > /dev/null 2>&1
}

if [ ${bamboo_repository_branch_name} = "main" ]; then
	echo "${GREEN}publishing docker image...$NONE"
	docker login -u $USER -p $SECRET $ARTIFACTORY
  if check_if_image_tag_exists ${IMAGE_VERSION}; then
    echo "Image - ${YELLOW}${IMAGE_NAME}:${IMAGE_VERSION}${NONE} already exists - ${RED}SKIPPING PUBLISHING${NONE}."
  else
    echo "Pushing Image - ${GREEN}${IMAGE_NAME}:${IMAGE_VERSION}${NONE}"
    docker push ${IMAGE_NAME}:${IMAGE_VERSION}
  fi
else
	echo "not on ${YELLOW}main${NONE} (${BLUE}$bamboo_repository_branch_name${NONE}) branch... ${RED}NOT PUBLISHING!${NONE}"
fi