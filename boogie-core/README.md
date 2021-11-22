## Boogie Core

## Module overview
<p>This module defines the core set of navigation interfaces used throughout the Boogie software as well as a collection of 
utility classes for commonly encountered situations when working with navigation data.</p>

### Table of contents

1. [Navigation interfaces](#navigation-interfaces) - nav interface definitions and simple concrete implementations
1. [Validation utilities](#validation-utilities) - nav validation utilities (leg content, sequencing, nav element de-duplication) 
1. [Procedure utilities](#procedure-utilities) - graphical procedure decorators, semantically queryable procedure decorators
1. [General utilities](#general-utilities) - 
1. [Viterbi algorithm](#viterbi-algorithm) -  

## Quick Start

### Navigation interfaces

The core Boogie module defines the common set of interfaces used to describe common navigation data by the application. These 
include but won't be limited to those in the following table:

| Interface | Simple Immutable/Buildable Implementation | Description |
|:----------|:------------------------------------------|:------------|
| [Airport](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse/boogie-core/src/main/java/org/mitre/tdp/boogie/Airport.java?at=refs%2Fheads%2Fmain) | [Boogie Airport](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse/boogie-core/src/main/java/org/mitre/tdp/boogie/model/BoogieAirport.java) |  |
| [Runway](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse/boogie-core/src/main/java/org/mitre/tdp/boogie/Runway.java?at=refs%2Fheads%2Fmain) | [Boogie Runway](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse/boogie-core/src/main/java/org/mitre/tdp/boogie/model/BoogieRunway.java) |  |
| [Fix](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse/boogie-core/src/main/java/org/mitre/tdp/boogie/Fix.java?at=refs%2Fheads%2Fmain) | [Boogie Fix](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse/boogie-core/src/main/java/org/mitre/tdp/boogie/model/BoogieFix.java) |  |
| [Leg](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse/boogie-core/src/main/java/org/mitre/tdp/boogie/Leg.java?at=refs%2Fheads%2Fmain) | [Boogie Leg](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse/boogie-core/src/main/java/org/mitre/tdp/boogie/model/BoogieLeg.java) |  |
| [Transition](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse/boogie-core/src/main/java/org/mitre/tdp/boogie/Transition.java?at=refs%2Fheads%2Fmain) | [Boogie Transition](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse/boogie-core/src/main/java/org/mitre/tdp/boogie/model/BoogieTransition.java) |  |
| [Procedure](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse/boogie-core/src/main/java/org/mitre/tdp/boogie/Procedure.java?at=refs%2Fheads%2Fmain) | [Boogie Procedure](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse/boogie-core/src/main/java/org/mitre/tdp/boogie/model/BoogieProcedure.java) |  |

Generally speaking all current and future implementations of (or additions to) these should live in the [org.mitre.tdp.boogie.model]((https://mustache.mitre.org/projects/TTFS/repos/boogie/browse/boogie-core/src/main/java/org/mitre/tdp/boogie/model?at=refs%2Fheads%2Fmain)) 
package and all implementations should be *immutable, buildable, and serializable*.

### Validation utilities

On top of the model interfaces Boogie provides access to a few relatively simple validation functions for [Leg implementations](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse/boogie-core/src/main/java/org/mitre/tdp/boogie/validate/PathTerminatorBasedLegValidator.java)  
or sequences of legs.

```java
Collection<? extends Leg> allLegs = ...;

// predicate which inspects the expected fields for each leg based on its PathTerminator and returns true if all 
// that are expected are present, otherwise false
PathTerminatorBasedLegValidator validator = new PathTerminatorBasedLegValidator();

Collection<Leg> validLegs = allLegs.stream().filter(validator).collect(Collectors.toList());

List<? extends Leg> shouldBeSequentialLegs = ...;

// throws exceptions if any of the legs are out of order in terms of their sequence number
EnforceSequentiallyOrderedLegs.INSTANCE.accept(shouldBeSequentialLegs); 
```

Additionally provided are utilities for electing unique records when multiple sources of data may have produced a copy of the 
same record (e.g. the HOBTT2 STAR into KATL from LIDO <i>and</i> CIFP). There are standard ways to match up similar instances of 
the same record across these source and so Boogie provides a straightforward API to de-duplicate them.

```java 
UnaryOperator<Collection<Procedure>> procedureElector = RecordElectorFactory.newUniqueProcedureElector();

// default implementations throw exceptions if multiples are present
Collection<Procedure> uniqueProcedures = procedureElector.apply(myMultiSourceProcedureCollection);

// but the exception throwing can be overridden with a supplied elector function
Function<List<Procedure>, Procedure> myDumbProcedureElector = procedures -> procedures.get(0);
 
UnaryOperator<Collection<Procedure>> procedureElector = RecordElectorFactory.newUniqueProcedureElector(myDumbProcedureElector);
```

### Procedure utilities

### General Utilities

### Viterbi algorithm 

For convenience Boogie also provides a collection of [buildable minimal implementations](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse/boogie-core/src/main/java/org/mitre/tdp/boogie/model?at=refs%2Fheads%2Fmain) in the ```org.mitre.tdp.boogie.model``` package. 
These implementations are immutable and serializable. Boogie also provides a collection of straightforward validator and de-duplication classes in the ```org.mitre.tdp.boogie.validate``` package - to tackle to common tasks 
of ensuring implementations of interfaces (e.g. ```Leg```) have appropriate field-level content for rudimentary flight-modeling. It also provides some de-duplication functions which can be used to elect representative versions of a procedure, airway, etc. 
if multiple exist in the downstream application (e.g. a CIFP vs a LIDO version of a procedure, or the same procedure from different cycles). 
