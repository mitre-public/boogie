#!/bin/bash
set -e

# This script exists to allow users to redeploy the Boogie REST service from CI by supplying a collection of input script
# arguments.
#
# Note - this script does require access to the bearer token for an account with appropriate permission in the target namespace
# of the target deployment cluster.
#
# The script take exactly 3 arguments:
#
# 1. The cluster server (e.g. https://api.epic-osc.mitre.org:6443)
# 2. A bearer token which contains the credential information for some account that can `kubectl apply` the change-set needed to
# redeploy the boogie service
# 3. The namespace on the kubernetes cluster targeted for deployment (note the account who's credentials we're using above must
# have permissions at the appropriate level for this namespace).

CLUSTER_SERVER=$1
SECRET=$2
NAMESPACE=$3

# Download the kubectl command line interface (doing this means this script should be run from within a docker container)

# Note - if an error like "SchemaError(com.coreos.operators.v2.OperatorCondition): invalid object doesn't have additional properties"
# is encountered when running this script its likely because the kubectl version has de-synced from that of the target cluster
curl --insecure -L https://dl.k8s.io/v1.21.2/bin/linux/amd64/kubectl -o /tmp/kubectl
chmod +rwx /tmp/kubectl

# These login and kube configuration step are following:
# https://blog.christianposta.com/kubernetes/logging-into-a-kubernetes-cluster-with-kubectl/

echo "Logging into the cluster $CLUSTER_SERVER"

echo "Setting credentials..."
/tmp/kubectl config set-credentials user-credentials --token="$SECRET"

echo "Setting cluster..."
/tmp/kubectl config set-cluster target-cluster --insecure-skip-tls-verify=true --server="$CLUSTER_SERVER"

echo "Setting context..."
/tmp/kubectl config set-context deploy-context \
--user="user-credentials" \
--namespace="$NAMESPACE" \
--cluster="target-cluster"

echo "Using context..."
/tmp/kubectl config use-context deploy-context

# Now that we're logged into the cluster use kubectl to apply any changes in the deployment scripts
# Note - bamboo auto-mounts the working build directory in the container (so we can do relative path work to find our deploy files)

echo "Apply configuration changes..."
/tmp/kubectl apply -f ${bamboo_working_directory}/deploy/kubernetes/config.boogie-rest.yaml
/tmp/kubectl apply -f ${bamboo_working_directory}/deploy/kubernetes/deployment.boogie-rest.yaml
/tmp/kubectl apply -f ${bamboo_working_directory}/deploy/kubernetes/service.boogie-rest.yaml