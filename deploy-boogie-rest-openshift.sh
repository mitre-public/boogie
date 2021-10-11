# Builds the baseline boogie rest endpoint deployment on the EPIC cluster
#
# This class builds and registers the images with the openshift container registry
# before applying local change set in the boogie-kube-deployment.yaml file
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
oc apply -f $SCRIPTPATH/boogie-rest-deployment.yaml
