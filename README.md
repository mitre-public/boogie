# Boogie
[![Build Status](https://pandafood.mitre.org/plugins/servlet/wittified/build-status/TTFS-VOIC)](https://https://pandafood.mitre.org/browse/CDA-SHIM)
[![Latest Release](https://img.shields.io/badge/version-0.0.91-gre.svg)](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse)

# Project Overview
Boogie serves as an entry point for navigation data parsing, route expansion, route conformance, and eventually flight modeling in a dependency-light software project. Boogie is a multi-module
software project with the specific components and their expected usages laid out in the module-specific readme's.

[boogie-core](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse/boogie-core) - Core set of navigational interfaces and general purpose algorithms

[boogie-arinc](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse/boogie-arinc) - Parser implementations for various versions and record types within ARINC-424

[boogie-routes](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse/boogie-routes) - Route expansion logic - takes route strings in a variety of formats and converts them into 2D paths using relevant navigational data.

[boogie-conformance](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse/boogie-conformance) - Tools for evaluating how well trajectory data conformed with a physical path (generally as outlined by a procedure/airway).

# Installation
Boogie leverages Gradle for builds and compiles/runs on Java 8 (though there are plans to improve this going forward). 

# Code Quality
[caasd-sonar](https://caasd-sonar.mitre.org/sonar/dashboard?id=boogie)