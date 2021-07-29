# Boogie REST

# Module overview

This module serves as a REST API wrapper around the collection of services provided in the other modules. The goal of this API is to facility queries for ARINC-formatted navigation data as well as 
on-the-fly route expansion. 

# Quick start

## Launching a REST service

To build and launch the API from CLI you can run (from the base ```/boogie``` folder):

1. ```./gradlew :boogie-rest:shadowJar``` - to build the uberjar containing the REST API
2. ```java -jar -Xmx5120m boogie-rest-<version>-all.jar``` - to run the mainClass from the jar i.e. the server start script. That's also around the recommended heap memory settings for the default
cache size and ARINC 424 source (CIFP).

Alternatively to do both in one step via Gradle for testing: ```./gradlew :boogie-rest:runShadow```. 

Two things to note:

1. The ARINC 424 data served by the REST API depends on the environment variable ```FILE_LOCATOR_PATH``` at the launch time of the API:
   1. If overridden with a value it should match the path spec expected by [PatternBasedFileLocator](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse/boogie-arinc/src/main/java/org/mitre/tdp/boogie/arinc/PatternBasedFileLocator.java?at=refs%2Fheads%2Fmain).
   2. Otherwise it defaults to the [MITRE CIFP](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse/boogie-arinc/src/main/java/org/mitre/tdp/boogie/arinc/ArincFileStore.java?at=main#25) archive.
2. When run locally the API documentation (hosted by Swagger) can be found under ```http://localhost:8087/boogie-arinc/``` (if run local, otherwise at the same path on your chosen host).

Reading the Swagger docs from a local launch of the software is the easiest way to see what the API provides even if you're unfamiliar with Clojure as a language and is the recommended way 
to check things out (if you need more information there is a later section in this readme which gives a bit more details about the internals of the API).

## Containerized deployment

For now it's a pain to sync the NFS-hosted 424 data from the netapp to one of our kubernetes clusters - in the future if we want to host an archive of the 424 data somewhere then this API can be containerized and the ENV variable can be configured to point to the file locations on the target server.

# Deep dives

## REST API detailed overview

For those who aren't familiar with Clojure and therefore don't want to spend a ton of time looking at the internals of the code itself this section exists to provide a slightly more detailed 
overview of the specifics of the REST API implementation.

[Clojure](https://www.braveclojure.com/clojure-for-the-brave-and-true/) is the non-Java language the majority of TDP developers prefer to use, so Boogie presents a thin REST API wrapper written 
in it to provide access to structured version of the 424 data for requested cycles. The service can be configured via a pair of environment variables:
 
1. ```CYCLE_CACHE_SIZE``` - this sets the maximum number of cycles to cache in the endpoint (default 3). The API by default indexes the latest available cycle at the ```FILE_LOCATOR_PATH``` on 
start-up.
2. ```FILE_LOCATOR_PATH``` - this is a templated path representing a collection of files containing 424 data into which we can substitute an airac cycle identifier to resolve a file (defaults to 
pointing at the MITRE CIFP archive).

### Caching details

Boogie provides some lightweight caching within the endpoint to support repeated queries from different users for the same cycle (e.g. a collection of real time applications querying for the most 
recent cycle of data). As such it pre-indexes the current navigation cycle on launch - however caching comes with some issues - namely hitting OOM problems due to caching too many nav cycles. The 
default configuration should avoid these given the quick-start mem settings are used and none of the other environment variables are modified.

However it's worth noting that the time to read->parse->convert a given cycle of navigational data from a file on disk is relatively short (10s-1min) depending on which source is being queried. As 
such it could be entirely appropriate for users to set the cache size=1 and eat a bit more overhead on request to avoid potential OOM/caching problems. 

Additional things to note - LIDO as a source is about 5x the size of CIFP for the same cycle and while the parsing has been tuned to attempt to minimize the amount of space used (and the amount  
of time it takes to parse a cycle) memory issues may always be a potential problem.

### Build details

To run the REST API Boogie takes advantage of the [clojurephant](https://github.com/clojurephant/clojurephant) library to compile the clojure REST API code from Gradle in-line with its Java code. 
Documentation for the REST API is configured via Swagger and is built into [reitit](https://github.com/metosin/reitit) which is the clojure software used to functionally declare the endpoints. 