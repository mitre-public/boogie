# Boogie
[![Build Status](https://pandafood.mitre.org/plugins/servlet/wittified/build-status/TTFS-SHIM)](https://pandafood.mitre.org/browse/TTFS-SHIM)
<br>
[![Latest Release](https://img.shields.io/badge/version-1.0.5-gre.svg)](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse)
<br>
[![Quality Gate Status](https://caasd-sonar.mitre.org/sonar/api/project_badges/measure?project=boogie&metric=alert_status)](https://caasd-sonar.mitre.org/sonar/dashboard?id=boogie)
<br>
[![Coverage](https://caasd-sonar.mitre.org/sonar/api/project_badges/measure?project=boogie&metric=coverage)](https://caasd-sonar.mitre.org/sonar/dashboard?id=boogie)
[![Lines of Code](https://caasd-sonar.mitre.org/sonar/api/project_badges/measure?project=boogie&metric=ncloc)](https://caasd-sonar.mitre.org/sonar/dashboard?id=boogie)
<br>
[![Reliability Rating](https://caasd-sonar.mitre.org/sonar/api/project_badges/measure?project=boogie&metric=reliability_rating)](https://caasd-sonar.mitre.org/sonar/dashboard?id=boogie)
[![Maintainability Rating](https://caasd-sonar.mitre.org/sonar/api/project_badges/measure?project=boogie&metric=sqale_rating)](https://caasd-sonar.mitre.org/sonar/dashboard?id=boogie)
[![Security Rating](https://caasd-sonar.mitre.org/sonar/api/project_badges/measure?project=boogie&metric=security_rating)](https://caasd-sonar.mitre.org/sonar/dashboard?id=boogie)
<br>
[![Bitbucket](https://img.shields.io/badge/Bitbucket-330F63?style=for-the-badge&logo=bitbucket&logoColor=white)](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse)
[![Bitbucket](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse)

### Project overview

Boogie serves as an entry point for navigation data parsing, route expansion, route conformance, and eventually flight modeling 
in a dependency-light software project.

### Repo table of contents

Boogie is a multi-module software project with the specific components and their expected usages laid out in the module-specific 
readme's (linked in the below table of contents).

A general outline of what's provided within this repo is here:

1. [boogie-core](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse/boogie-core) - core set of navigational interfaces and general purpose algorithms
1. [boogie-arinc](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse/boogie-arinc) - parser implementations for various versions and record types within ARINC-424
1. [boogie-routes](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse/boogie-routes) - route expansion logic - takes route strings in a variety of formats and converts them into 2D paths using relevant navigational data.
1. [boogie-conformance](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse/boogie-conformance) - tools for evaluating how well trajectory data conformed with a physical path (generally as outlined by a procedure/airway). 
1. [boogie-rest](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse/boogie-rest) - REST API wrapper around common Boogie services - utilizes pieces of multiple modules (e.g. ARINC parsing, Route Expansion).
1. [boogie-test](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse/boogie-test) - is provided as a common module for integration testing across components of the above
1. [CI services](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse/bamboo-specs) - which outlines the CI pipeline for Boogie and the artifacts/services it produces

### Resources

Outlined here are the generic set of resources related to the project:

1. [CI plan](https://pandafood.mitre.org/browse/TTFS-SHIM)
1. [Code coverage and quality](https://caasd-sonar.mitre.org/sonar/dashboard?id=boogie)
1. [Openshift project information](https://gitlab.mitre.org/tfm-analytics-ec/tfm-analytics-openshift-management)
1. [Internal REST service deployment](https://boogie-rest.apps.epic-osc.mitre.org/boogie/index.html)
1. [Software guidance](https://gitlab.mitre.org/tfm-analytics-ec/project-documentation/-/blob/main/software-guidance-and-best-practices/README.md)
1. [TDP-infrastructure](https://mustache.mitre.org/projects/TTFS/repos/ttfs/browse/tdp-infrastructure) & [TDP-procedure](https://mustache.mitre.org/projects/TTFS/repos/ttfs/browse/ttfs-procedure)
1. [AKELA-boogie](https://mustache.mitre.org/projects/AKELA/repos/akela-boogie/browse)