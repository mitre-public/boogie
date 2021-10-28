# Similar to boogie-kube-deploy.sh this class registers the kube-copy arinc data job with
# the epic cluster and uploads the docker image to cluster registry that performs the copy
# task

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

echo "Using dockerfiles $SCRIPTPATH/Dockerfile..."

# Build and tag the frontend image for deployment
docker build -f $SCRIPTPATH/../../cifp-download/Dockerfile -t cifp-download .
docker tag cifp-download $REGISTRY_URL/tfm-analytics/cifp-download

# Push to docker repository triggering and auto redeploy
docker push $REGISTRY_URL/tfm-analytics/cifp-download

# Apply openshift config changes
oc project tfm-analytics
oc apply -f $SCRIPTPATH/../kubernetes/cifp-download-kubejob.yaml