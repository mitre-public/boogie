# Boogie REST

# Module overview

This module serves as a REST API wrapper around the collection of services provided in the other modules. The goal of this API is to facility queries for ARINC-formatted navigation data as well as 
on-the-fly route expansion. 

# Quick start

## Launching a REST service

To build and launch the API from CLI you can run (from the base ```/boogie``` folder):

1. ```./gradlew :boogie-rest:shadowJar``` - to build the uberjar containing the REST API
2. ```java -jar -Xmx5120m boogie-rest-<version>-all.jar``` - to run the mainClass from the jar i.e. the server start script. That includes the recommended heap memory settings for a cycle of CIFP.

Alternatively to do both in one step via Gradle for testing: ```./gradlew :boogie-rest:runShadow```. 

Two things to note:

1. The ARINC 424 data served by the REST API depends on the environment variable ```FILE_LOCATOR_PATH``` at the launch time of the API:
   1. If overridden with a value it should match the path spec expected by [PatternBasedFileLocator](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse/boogie-arinc/src/main/java/org/mitre/tdp/boogie/arinc/PatternBasedFileLocator.java?at=refs%2Fheads%2Fmain).
   2. Otherwise it defaults to the [MITRE CIFP](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse/boogie-arinc/src/main/java/org/mitre/tdp/boogie/arinc/ArincFileStore.java?at=main#25) archive.
2. When run locally the API documentation (hosted by Swagger) can be found under ```http://localhost:8087/boogie/``` (if run local, otherwise at the same path on your chosen host).

Reading the Swagger docs from a local launch of the software is the easiest way to see what the API provides even if you're unfamiliar with Clojure as a language and is the recommended way 
to check things out (if you need more information there is a later section in this readme which gives a bit more details about the internals of the API).

## Containerized deployment

For now it's a pain to sync the NFS-hosted 424 data from the netapp to one of our kubernetes clusters - in the future if we want to host an archive of the 424 data somewhere then this API can be 
containerized and the ENV variable can be configured to point to the file locations on the target server.

Generally Boogie views the ingest and availability of cycles of infrastructure data to be outside its scope. The recommendation for scaling out the service is to have an external process handle the 
ingest and availability of the data - and then use kubernetes to handle tearing down and standing up a new container pointed at the latest cycle of infrastructure data (rather than forcing the application 
to handle swapping out its cache internally and be forced to watch directories, etc.).

# Context

## Build details

[Clojure](https://www.braveclojure.com/clojure-for-the-brave-and-true/) is the non-Java language the majority of TDP developers prefer to use (since its still a JVM language). Boogie takes advantage of 
the [clojurephant](https://github.com/clojurephant/clojurephant) library to compile the clojure REST API code from Gradle in-line with its Java code. Documentation for the REST API is configured via Swagger 
and is built into [reitit](https://github.com/metosin/reitit) which is the clojure software used to functionally declare the endpoints. 

These tools let us keep the REST API implementation close to the underlying Java code powering the library while also letting us take advantage of the flexibility and ease of use of clojure tooling 
for standing one up.