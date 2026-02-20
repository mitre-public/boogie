# Boogie
[![publish](https://github.com/mitre-public/boogie/actions/workflows/publish.yml/badge.svg)](https://github.com/mitre-public/boogie/actions/workflows/publish.yml)

### Project overview

Boogie serves as an entry point for navigation data parsing, route expansion, route conformance, and eventually flight modeling 
in a dependency-light software project.

### Repo table of contents

Boogie is a multi-module software project with the specific components and their expected usages laid out in the module-specific 
readme's (linked in the below table of contents).

A general outline of what's provided within this repo is here:

1. [boogie-core](https://github.com/mitre-public/boogie/tree/main/boogie-core) - core set of navigational interfaces and general purpose algorithms
1. [boogie-arinc](https://github.com/mitre-public/boogie/tree/main/boogie-arinc) - parser implementations for various versions and record types within ARINC-424
1. [boogie-routes](https://github.com/mitre-public/boogie/tree/main/boogie-routes) - route expansion logic - takes route strings in a variety of formats and converts them into 2D paths using relevant navigational data.
1. [boogie-conformance](https://github.com/mitre-public/boogie/tree/main/boogie-conformance) - tools for evaluating how well trajectory data conformed with a physical path (generally as outlined by a procedure/airway).
1. [boogie-util](https://github.com/mitre-public/boogie/tree/main/boogie-util) - tools for AIRAC and Magnetic Variation.
1. [boogie-dafif](https://github.com/mitre-public/boogie/tree/main/boogie-dafif) - parser implementations for DAFIF 8.1 (Digital Aeronautical Flight Information File) with assembly support for airports, fixes, airways, and procedures.
1. [boogie-xml](https://github.com/mitre-public/boogie/tree/main/boogie-xml) - WIP API for interacting with ARINC 424 XML