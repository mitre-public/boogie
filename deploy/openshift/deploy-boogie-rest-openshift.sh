# Runs the deployment of the boogie rest endpoint to the internal openshift cluster

# This script should be run from CLI only and will ensure the image is pushed to the right namespace
# on the remote cluster
set -e

SCRIPTPATH="$( cd "$(dirname "$0")" >/dev/null 2>&1 ; pwd -P )"
CLUSTER_URL="https://api.epic-osc.mitre.org:6443"

# Log into openshift cluster and docker repository
oc_server="$(oc whoami --show-server)"
if [[ ("$oc_server" != "$CLUSTER_URL") || (! $(oc whoami)) ]]
then
	oc login --token="$1" --server="$CLUSTER_URL"
else
    echo "Already logged in to oc as $(oc whoami)"
fi

# Apply openshift config changes
oc project tfm-analytics
kubectl apply -f $SCRIPTPATH/../kubernetes/boogie-rest-config.yaml
kubectl apply -f $SCRIPTPATH/../kubernetes/boogie-rest-deployment.yaml
kubectl apply -f $SCRIPTPATH/../kubernetes/boogie-rest-service.yaml
