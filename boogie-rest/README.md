# Boogie REST

Boogie provides a [REST API](https://boogie.streaming.cre.gov.aws.mitre.org/swagger-ui/index.html#/) which - when configured to 
point to raw data in the ARINC-424 format - provides access to the following REST endpoints:

1. Access to [parsed versions](../boogie-arinc/src/main/java/org/mitre/tdp/boogie/arinc/model) of the underlying 424 data.
2. Access to [modeled & assembled versions](../boogie-core/src/main/java/org/mitre/tdp/boogie/model) of the 424 data (e.g. as 
composite procedures, airways, and airports).
3. Access to the [RouteExpander](../boogie-routes/src/main/java/org/mitre/tdp/boogie/alg/RouteExpander.java) backed by the hosted 
424 data.

The core artifacts produced for the REST API are (both hosted on [Codev Artifactory](https://repo.codev.mitre.org)):
1. [Docker Image](https://repo.codev.mitre.org/artifactory/webapp/#/artifacts/browse/tree/General/idaass-docker/tdp/boogie-rest) - release-able 
on demand alongside the helm chart
2. [Helm Chart](https://repo.codev.mitre.org/artifactory/webapp/#/artifacts/browse/tree/General/idaas-helm/boogie-rest-2.0.2-release-e88d97c.tgz) - 
as well as other versions - each chart release version has an associated image version (though the chart defaults to using the - latest, 
see [Generating From Helm](#generating-from-helm))

### Building and running the endpoint locally

The recommended way to do testing and new development is to run the endpoint locally running on some of the embedded raw data in the
repo. Doing so is as easy as:

```bash
# from the boogie directory - this will create an image boogie-rest:latest which is referenced in the docker-compose file
(base) MM******-PC:boogie acramer$ docker build -t boogie-rest:latest --build-arg MAVEN_USER=<codev-user> --build-arg MAVEN_PASSWORD=<codev-password> .

# still from the boogie directory - this will stand up and run the REST API which has bind-mounted into it some of our local 
# unit testing files 
(base) MM******-PC:boogie acramer$ docker-compose up

# the REST API(s) are by default hosted in localhost:8080/{arinc/routes} - or go to swagger at localhost:8080/swagger
```

### Generating from Helm

Boogie also provides a simple Helm chart which contains packaged information on how to stand the service up via Kubernetes. 

By default the container watches a directory on a visible filesystem for new ARINC 424 files. When new files arrive in the watched 
directory they are automatically processed and the endpoints cache of navigation data is updated (and older data is evicted).

There are two key classes of configuration options for the endpoint, outlined as follows.

#### REST API Configuration

REST endpoint configuration options modify where Boogie looks for data and how it interacts with new data when encountered in those 
locations. These are all provided in an `arinc.properties` file who's relative location is passed to the container via an environment 
variable `arinc.config.path`. In helm these are directly overridable in a `values.yml` file as a block.

[See the defaults](../helm/values.yaml)

| parameter              | description                                                                                                                                                                                                                                                                                                                                        | required? (if no, the default)                                                           | type            |
|:-----------------------|:---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:-----------------------------------------------------------------------------------------|:----------------|
| arinc.watchDirectory   | The directory to watch for new ARINC 424 files, new files appearing in this directory will be read in and evict the current cache of infrastructure available behind the REST API.                                                                                                                                                                 | yes                                                                                      | path            |
| cleanup.watchDirectory | When set to `true` Boogie will automatically cleanup any raw files in the directory other than the one currently hosted behind the API. This is useful to (1) keep data size down in the mounted directory and (2) to allow quick recovery of the API state on container restarts (since the container will reload and recache any present files). | no, `false`                                                                              | boolean         |
| arinc.filePattern      | If set only files matching the pattern that appear in the `arinc.watchDirectory` will be used to update the cache.                                                                                                                                                                                                                                 | no, `matchAll` (all new files entering the directory will trigger a reload of the cache) | regex string    |


#### Kubernetes Configuration

Kubernetes deployment configuration options modify things like the deployed image version, resource allocations and other tunable 
knobs particular to various deployments of the REST API (e.g. target namespace, PVC storage class, etc.). All of these values are 
set in you `values.yml` file when using the chart.

[See the defaults](../helm/values.yaml)

| parameter         | description                                                                                                                                                                                              | required (if no, the default)                                      | type    |
|:------------------|:---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:-------------------------------------------------------------------|:--------|
| storageClassName  | The name of PVC storageClass to use - for cloud vs openshift deployments (and depending on your kubernetes provider this may vary. Note that the volume class should allow the `ReadWriteMany` policy    | no, `efs-auto-provisioned` (this is a CRE RWM volume type          | string  |
| annotations       | The collection of annotations to apply to the pods when launched.                                                                                                                                        | no, defaulted to prometheus service auto-discovery annotations     | yaml    |
| image             | To override the image version which Helm will use for deployments                                                                                                                                        | no, `repo.codev.mitre.org/idaass-docker/tdp/boogie-rest:latest`    | string  |

For further example information see the [values.yaml](https://github.com/mitre-tdp/auspicious-abyss/blob/main/deploy/boogie/boogie.values.yaml) 
file used in the prod CRE deployment of the API.