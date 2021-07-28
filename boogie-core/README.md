# Boogie Core
[![Build Status](https://pandafood.mitre.org/plugins/servlet/wittified/build-status/TTFS-VOIC)](https://https://pandafood.mitre.org/browse/CDA-SHIM)
[![Latest Release](https://img.shields.io/badge/version-0.0.91-gre.svg)](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse)

# Module overview
<p>This module defines the core set of navigation interfaces used throughout the Boogie software as well as a collection of utility classes for commonly 
encountered situations when working with navigation data.</p>

# Quick Start

## Navigation interfaces

The core Boogie module defines the common set of interfaces used to describe common navigation data by the application. These include but won't be limited to:

1. [Airports](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse/boogie-core/src/main/java/org/mitre/tdp/boogie/Airport.java?at=refs%2Fheads%2Fmain)
2. [Runways](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse/boogie-core/src/main/java/org/mitre/tdp/boogie/Runway.java?at=refs%2Fheads%2Fmain)
3. [Airways](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse/boogie-core/src/main/java/org/mitre/tdp/boogie/Airway.java?at=refs%2Fheads%2Fmain)
4. [Fixes](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse/boogie-core/src/main/java/org/mitre/tdp/boogie/Fix.java?at=refs%2Fheads%2Fmain)
5. [Legs](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse/boogie-core/src/main/java/org/mitre/tdp/boogie/Leg.java?at=refs%2Fheads%2Fmain)
6. [Transitions](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse/boogie-core/src/main/java/org/mitre/tdp/boogie/Transition.java?at=refs%2Fheads%2Fmain) / [Procedures](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse/boogie-core/src/main/java/org/mitre/tdp/boogie/Procedure.java?at=refs%2Fheads%2Fmain)

For convenience Boogie also provides a collection of [buildable minimal implementations](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse/boogie-core/src/main/java/org/mitre/tdp/boogie/model?at=refs%2Fheads%2Fmain) in the ```org.mitre.tdp.boogie.model``` package. 
These implementations are immutable and serializable. Boogie also provides a collection of straightforward validator and de-duplication classes in the ```org.mitre.tdp.boogie.validate``` package - to tackle to common tasks 
of ensuring implementations of interfaces (e.g. ```Leg```) have appropriate field-level content for rudimentary flight-modeling. It also provides some de-duplication functions which can be used to elect representative versions of a procedure, airway, etc. 
if multiple exist in the downstream application (e.g. a CIFP vs a LIDO version of a procedure, or the same procedure from different cycles). 
