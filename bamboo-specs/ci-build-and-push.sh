#!/bin/bash
set -e

NONE="$(echo -e "\033[0m")"
RED="$(echo -e "\033[31m")"
GREEN="$(echo -e "\033[32m")"
YELLOW="$(echo -e "\033[33m")"
BLUE="$(echo -e "\033[34m")"

IMAGE_NAME=${bamboo_artifactory_base}/${bamboo_artifactory_namespace}/boogie-rest
echo "Image Name: $IMAGE_NAME"

BOOGIE_VERSION=$(./gradlew properties --no-daemon --console=plain -q | grep "^version:" | awk '{printf $2}')
echo "Boogie Version: $BOOGIE_VERSION"

CURRENT_COMMIT=$(git rev-parse --short HEAD)
echo "Current Commit: $CURRENT_COMMIT"

IMAGE_VERSION=${BOOGIE_VERSION}-${CURRENT_COMMIT}
echo "Image Version: $IMAGE_VERSION"

docker build . -t ${IMAGE_NAME}:${IMAGE_VERSION} --format docker

if [ ${bamboo_repository_branch_name} = "main" ]; then
	echo "${GREEN}publishing a docker image...$NONE"
	docker login -u ${bamboo_artifactory_user} -p ${bamboo_artifactory_password} ${bamboo_artifactory_base}

	docker push ${IMAGE_NAME}:${IMAGE_VERSION}
else
	echo "not on ${YELLOW}main${NONE} (${BLUE}$bamboo_repository_branch_name${NONE}) branch... ${RED}NOT PUBLISHING!${NONE}"
fi