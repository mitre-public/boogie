#!/bin/bash
set -e

# Download the kubectl command line interface (doing this means this script should be run from within a docker container)

# Note - if an error like "SchemaError(com.coreos.operators.v2.OperatorCondition): invalid object doesn't have additional properties"
# is encountered when running this script its likely because the kubectl version has de-synced from that of the target cluster
curl --insecure -L https://dl.k8s.io/v1.21.2/bin/linux/amd64/kubectl -o /tmp/kubectl
chmod +rwx /tmp/kubectl

# These login and kube configuration step are following:
# https://blog.christianposta.com/kubernetes/logging-into-a-kubernetes-cluster-with-kubectl/

echo "Logging into the cluster ${bamboo_internal_kubernetes_cluster}..."

echo "Setting credentials..."
/tmp/kubectl config set-credentials user-credentials --token="${bamboo_internal_kubernetes_deployment_secret}"

echo "Setting cluster..."
/tmp/kubectl config set-cluster target-cluster --insecure-skip-tls-verify=true --server="${bamboo_internal_kubernetes_cluster_server}"

echo "Setting context..."
/tmp/kubectl config set-context deploy-context \
--user="user-credentials" \
--namespace="${bamboo_internal_kubernetes_namespace}" \
--cluster="target-cluster"

echo "Using context..."
/tmp/kubectl config use-context deploy-context

# Now that we're logged into the cluster use kubectl to apply any changes in the deployment scripts
# Note - bamboo auto-mounts the working build directory in the container (so we can do relative path work to find our deploy files)

echo "Apply configuration changes..."
/tmp/kubectl apply -f ${bamboo_working_directory}/deploy/kubernetes/boogie-rest-config.yaml
/tmp/kubectl apply -f ${bamboo_working_directory}/deploy/kubernetes/boogie-rest-deployment.yaml
/tmp/kubectl apply -f ${bamboo_working_directory}/deploy/kubernetes/boogie-rest-service.yaml