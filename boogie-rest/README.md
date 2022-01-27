## Boogie REST

## Module overview

This module serves as a REST API wrapper around the collection of services provided in the other modules. The goal of this API is to facility queries for ARINC-formatted navigation data as well as 
on-the-fly route expansion. 

## Quick start

### Launching a REST service

To build and launch the API from CLI you can run (from the base ```/boogie``` folder):

1. ```./gradlew :boogie-rest:shadowJar``` - to build the uberjar containing the REST API
2. ```java -jar -Xmx5120m boogie-rest-<version>-all.jar``` - to run the mainClass from the jar i.e. the server start script. That includes the recommended heap memory settings for a cycle of CIFP.

Alternatively to do both in one step via Gradle for testing: ```./gradlew :boogie-rest:runShadow```. 

Two things to note:

1. The ARINC 424 data served by the REST API depends on the environment variable ```FILE_LOCATOR_PATH``` at the launch time of the API:
   1. If overridden with a value it should match the path spec expected by [PatternBasedFileLocator](https://github.com/mitre-tdp/boogie/blob/main/boogie-arinc/src/main/java/org/mitre/tdp/boogie/arinc/PatternBasedFileLocator.java).
   2. Otherwise it defaults to the [MITRE CIFP](https://github.com/mitre-tdp/boogie/blob/main/boogie-arinc/src/main/java/org/mitre/tdp/boogie/arinc/ArincFileStore.java) archive.
2. When run locally the API documentation (hosted by Swagger) can be found under ```http://localhost:8087/boogie/``` (if run local, otherwise at the same path on your chosen host).

Reading the Swagger docs from a local launch of the software is the easiest way to see what the API provides even if you're unfamiliar with Clojure as a language and is the recommended way 
to check things out (if you need more information there is a later section in this readme which gives a bit more details about the internals of the API).

### Containerized deployment

Boogie provides a docker image for the containerized version of the REST API within [MITRE artifactory](https://artifacts.mitre.org/artifactory/webapp/#/artifacts/browse/tree/Properties/docker/tdp/boogie-rest). The image version is 
the short name of the git commit it was built off of plus the current version of the boogie software project. The image needs to be deployed with a mounted cycle of infrastructure data as per the `Launching a REST service` portion of 
the README. Locally the docker container can be launched via:
```shell script
# build the image
docker-compose build --build-arg MAVEN_USER=<codev-user> --build-arg MAVEN_PASSWORD=<codev-password>

# launch the image
docker-compose up
```
Which will launch the container and make the service available on `localhost:24567` with a small collection of infrastructure data around/related to KJFK for testing/etc.

Boogie also hosts a deployment of the REST service on the internal MITRE EPIC-OSC cluster. The kubernetes service itself is deployable standalone and expects to be able to find a cycle of ARINC 424 data in a 
pre-configured PVC (persistent volume claim) that will be read only once and indexed by the REST endpoint on start up. The PVC should be mounted at `/data/db/arinc` and the service will index any file matching the 
name `arinc-data.dat`.

This deployment can be updated by running:
```shell script
./deploy-boogie-rest.sh
```
This will re-build the docker images, push them to the EPIC-OSC container registry, and then update the deployment configuration outlined in the `boogie-kube-deployment.yaml` file. The actual container is hosted at 
`boogie-rest.apps.epic-osc.mitre.org/boogie/` which will be the swagger documentation of the API.

Provided alongside this deployment in the `./cifp-download` directory is a separate scheduled KubeJob which is configured to pull the latest cycle of CIFP data from the FAA web servers every day at midnight and sync it to 
the PVC mounted on the REST server. Since the rest server only re-reads data from disk on initialization in order to reflect updates in the PVC data it needs to be restarted once the sync has occurred.

## Context

### Build details

[Clojure](https://www.braveclojure.com/clojure-for-the-brave-and-true/) is the non-Java language the majority of TDP developers prefer to use (since its still a JVM language). Boogie takes advantage of 
the [clojurephant](https://github.com/clojurephant/clojurephant) library to compile the clojure REST API code from Gradle in-line with its Java code. Documentation for the REST API is configured via Swagger 
and is built into [reitit](https://github.com/metosin/reitit) which is the clojure software used to functionally declare the endpoints. 

These tools let us keep the REST API implementation close to the underlying Java code powering the library while also letting us take advantage of the flexibility and ease of use of clojure tooling 
for standing one up.