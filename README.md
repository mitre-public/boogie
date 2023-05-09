# Boogie
![Build Status](https://github.com/mitre-tdp/boogie/actions/workflows/test.yaml/badge.svg)
<br>
[![Latest Release](https://img.shields.io/badge/version-2.0.1-gre.svg)](https://github.com/mitre-tdp/boogie)
[![Concourse Pipeline](https://img.shields.io/badge/Concourse-Pipeline-blue)](https://concourse-cre-ops.cre.gov.aws.mitre.org/teams/cre-streaming/pipelines/boogie)

**NOTE**: If you're coming here from a local installation of Boogie (the one on MITRE bitbucket), please ensure you:
```bash
git remote set-url origin git@github.com:mitre-tdp/boogie.git
```
From within your existing repo to update the remote location of the Boogie repo to its new home in Github.

**NOTE**: Github hosted Boogie has been updated to Java 11 - if you're still primarily using Java 8 the recommended way to run is:
```bash
./gradlew <command> -Dorg.gradle.java.home=/path/to/your/jdk11/install
```

### Project overview

Boogie serves as an entry point for navigation data parsing, route expansion, route conformance, and eventually flight modeling 
in a dependency-light software project.

### Repo table of contents

Boogie is a multi-module software project with the specific components and their expected usages laid out in the module-specific 
readme's (linked in the below table of contents).

A general outline of what's provided within this repo is here:

1. [boogie-core](https://github.com/mitre-tdp/boogie/tree/main/boogie-core) - core set of navigational interfaces and general purpose algorithms
1. [boogie-arinc](https://github.com/mitre-tdp/boogie/tree/main/boogie-arinc) - parser implementations for various versions and record types within ARINC-424
1. [boogie-routes](https://github.com/mitre-tdp/boogie/tree/main/boogie-routes) - route expansion logic - takes route strings in a variety of formats and converts them into 2D paths using relevant navigational data.
1. [boogie-conformance](https://github.com/mitre-tdp/boogie/tree/main/boogie-conformance) - tools for evaluating how well trajectory data conformed with a physical path (generally as outlined by a procedure/airway).

### Resources

Outlined here are the generic set of resources related to the project:

1. [CI tasks](https://github.com/mitre-tdp/boogie/actions)
1. [Cloud REST service deployment (CRE)](https://cre.mitre.org/streaming/boogie/swagger-ui/index.html)
1. [Software guidance](https://gitlab.mitre.org/tfm-analytics-ec/project-documentation/-/blob/main/software-guidance-and-best-practices/README.md)
1. [TDP-infrastructure](https://mustache.mitre.org/projects/TTFS/repos/ttfs/browse/tdp-infrastructure) & [TDP-procedure](https://mustache.mitre.org/projects/TTFS/repos/ttfs/browse/ttfs-procedure)
1. [AKELA-boogie](https://mustache.mitre.org/projects/AKELA/repos/akela-boogie/browse)
