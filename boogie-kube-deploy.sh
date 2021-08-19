set -e

SCRIPTPATH="$( cd "$(dirname "$0")" >/dev/null 2>&1 ; pwd -P )"
CLUSTER_URL="https://api.epic-osc.mitre.org:6443"
REGISTRY_URL="default-route-openshift-image-registry.apps.epic-osc.mitre.org"

# Log into openshift cluster and docker repository
oc_server="$(oc whoami --show-server)"
if [[ ("$oc_server" != "$CLUSTER_URL") || (! $(oc whoami)) ]]
then
	oc login --token="$1" --server="$CLUSTER_URL"
else
    echo "Already logged in to oc as $(oc whoami)"
fi

docker login -u $(oc whoami) -p $(oc whoami -t) $REGISTRY_URL

echo "Using dockerfiles $SCRIPTPATH/docker/Dockerfile..."

# Build and tag the frontend image for deployment
docker build -f $SCRIPTPATH/Dockerfile -t boogie-rest .
docker tag boogie-rest $REGISTRY_URL/tdp/boogie-rest

# Push to docker repository triggering and auto redeploy
docker push $REGISTRY_URL/tdp/boogie-rest

# Apply openshift config changes
oc project tdp
oc apply -f $SCRIPTPATH/boogie-kube-deployment.yaml
