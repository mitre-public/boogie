# Boogie Core

## Module overview
<p>This module defines the core set of navigation interfaces used throughout the Boogie software as well as a collection of 
utility classes for commonly encountered situations when working with navigation data.</p>

### Table of contents

1. [Navigation interfaces](#navigation-interfaces) - nav interface definitions and simple concrete implementations
1. [General utilities](#general-utilities) - pairwise/triples iteration/streams over/from collections, magnetic models, coordinate parsing
1. [Viterbi algorithm](#viterbi-algorithm) - easily extensible implementation of the Viterbi Algorithm 
1. [Procedure utilities](#procedure-utilities) - graphical procedure decorators, semantically queryable procedure decorators
1. [Validation utilities](#validation-utilities) - nav validation utilities (leg content, sequencing, nav element de-duplication)

## Quick Start

### Navigation interfaces

The core Boogie module defines the common set of interfaces used to describe common navigation data by the application. The general 
philosophy behind these interfaces has been to keep then *as thin as possible* allowing for extension as needed when new use cases 
arise. For now these interfaces include but won't be limited to those in the following table:

| Interface | Simple Immutable/Buildable Implementation | Description |
|:----------|:------------------------------------------|:------------|
| [Airport](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse/boogie-core/src/main/java/org/mitre/tdp/boogie/Airport.java?at=refs%2Fheads%2Fmain) | [Boogie Airport](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse/boogie-core/src/main/java/org/mitre/tdp/boogie/model/BoogieAirport.java) | The baseline airport definition is usable as a fix but provides its own name/region overrides and access to a collection of available runways. |
| [Runway](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse/boogie-core/src/main/java/org/mitre/tdp/boogie/Runway.java?at=refs%2Fheads%2Fmain) | [Boogie Runway](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse/boogie-core/src/main/java/org/mitre/tdp/boogie/model/BoogieRunway.java) | The runway implementation provides access to the arrival/departure end locations, a width + length (for bounding box computations) and a course + name. |
| [Fix](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse/boogie-core/src/main/java/org/mitre/tdp/boogie/Fix.java?at=refs%2Fheads%2Fmain) | [Boogie Fix](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse/boogie-core/src/main/java/org/mitre/tdp/boogie/model/BoogieFix.java) | A fix represents a named location within a region, a magnetic variation (potentially published) but always a [modeled value](#magnetic-modeling), and potentially an elevation. Generally fixes *should* represent airports, waypoints, or navaids. |
| [Leg](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse/boogie-core/src/main/java/org/mitre/tdp/boogie/Leg.java?at=refs%2Fheads%2Fmain) | [Boogie Leg](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse/boogie-core/src/main/java/org/mitre/tdp/boogie/model/BoogieLeg.java) | The leg definition contains just the information required to fly the leg as an aircraft would and is reminiscent of the fields required by its 424 coding. The leg additionally contains a few boolean values for indicating the type of leg (e.g. flyover, holding, initial fix, intermediate approach fix, or final approach course fix. |
| [Transition](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse/boogie-core/src/main/java/org/mitre/tdp/boogie/Transition.java?at=refs%2Fheads%2Fmain) | [Boogie Transition](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse/boogie-core/src/main/java/org/mitre/tdp/boogie/model/BoogieTransition.java) | Transitions represent a linear sequence of legs within a larger procedure and are associated with both a [transition type](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse/boogie-core/src/main/java/org/mitre/tdp/boogie/TransitionType.java) and [procedure type](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse/boogie-core/src/main/java/org/mitre/tdp/boogie/ProcedureType.java) as well as an optional identifier. |
| [Procedure](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse/boogie-core/src/main/java/org/mitre/tdp/boogie/Procedure.java?at=refs%2Fheads%2Fmain) | [Boogie Procedure](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse/boogie-core/src/main/java/org/mitre/tdp/boogie/model/BoogieProcedure.java) | Procedures represent collections of transitions and on top of providing naming information also provide high level [required navigation equipage](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse/boogie-core/src/main/java/org/mitre/tdp/boogie/RequiredNavigationEquipage.java). |

Generally speaking all current and future implementations of (or additions to) these should live in the [org.mitre.tdp.boogie.model]((https://mustache.mitre.org/projects/TTFS/repos/boogie/browse/boogie-core/src/main/java/org/mitre/tdp/boogie/model?at=refs%2Fheads%2Fmain)) 
package and all implementations should be <b>*immutable, buildable, and serializable*</b>.

### General Utilities

At a high-level Boogie provides a small collection of generally-useful utilities for working with navigation (particularly 
procedure and leg data) which can be re-used in a relatively straightforward fashion.

#### Magnetic modeling

Boogie provides a future looking (as well as historical) collection of declination information at any point on the globe via the 
[Declinations](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse/boogie-core/src/main/java/org/mitre/tdp/boogie/util/Declinations.java?at=refs%2Fheads%2Fmain) 
class. This is generally useful for modeling magvars on the fly off waypoints when they are used as fix-radial-distances (FRDs) 
within real-world flightplans.

```java
double declination = Declinations.declination(myLat, myLon, myOptionalAltitudeInFeet, currentTime);
```

#### Pairwise and triples collection iteration

Due to the fact that most legs in procedures are considered sequentially as pairs (e.g. RF/TF - need to know the terminating fix 
of the previous leg to understand the path) and occasionally for certain leg combinations triples, boogie provides pre-canned 
iterator classes for enumerating collections in that way. See:

1. [Iterators](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse/boogie-core/src/main/java/org/mitre/tdp/boogie/util/Iterators.java?at=main)
1. [Streams](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse/boogie-core/src/main/java/org/mitre/tdp/boogie/util/Streams.java?at=main)

```java
List<Leg> legs = ...;

BiConsumer<Leg, Leg> pairwiseLegConsumer = ...;

// enumerate the list applying the consumer to consecutive elements
Iterators.pairwise(legs, pairwiseLegConsumer);

// alternatively turn it into a pairwise stream of elements
Stream<Pair<Leg, Leg>> pairStream = Streams.pairwise(legs);
```

The `Iterators` interface also provides methods for `fastSlow` iteration of lists given provided element predicates e.g.

```java
Predicate<Leg> isTf = leg -> PathTerminator.TF.equals(leg.pathTerminator());

// between may be empty if TFs are sequential
TriPredicate<Leg, Leg, List<Leg>> legConsumer = (first, last, between) -> ...;

Iterators.fastSlow(legs, isTf, legConsumer);
```

as well as additional ones where the open/close criteria may be different. Taken all-together these provide a convenient set of 
methods for iterating through collections of navigation elements, particularly legs.

### Viterbi algorithm

All source code lives within the [org.mitre.tdp.boogie.viterbi](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse/boogie-core/src/main/java/org/mitre/tdp/boogie/viterbi) 
package with the main entrypoint being via the [ViterbiTagger](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse/boogie-core/src/main/java/org/mitre/tdp/boogie/viterbi/ViterbiTagger.java).

**Note**: more holistic documentation for the tagger and how it works is a WIP.

### Procedure utilities
 
Boogie (core) provides a pair of patterns for accessing and working with procedures more sensibly - these are:

1. [QueryableProcedure](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse/boogie-core/src/main/java/org/mitre/tdp/boogie/model/QueryableProcedure.java)
1. [ProcedureGraph](https://mustache.mitre.org/projects/TTFS/repos/boogie/browse/boogie-core/src/main/java/org/mitre/tdp/boogie/model/ProcedureGraph.java)

Both are simple decorators for `Procedure` implementing classes which implement `Procedure` themselves. There are others decorators 
introduced in downstream modules but the general programmatic way to use these is something like (taking for example with the 
HOBTT2 procedure shown below):

<a href="https://mustache.mitre.org/projects/TTFS/repos/boogie/raw/boogie-core/docs/hobtt2-1.png?at=refs%2Fheads%2Fmain">
<img align="float: left;" height="500" src="https://mustache.mitre.org/projects/TTFS/repos/boogie/raw/boogie-core/docs/hobtt2-1.png?at=refs%2Fheads%2Fmain"/>
</a>
<a href="https://mustache.mitre.org/projects/TTFS/repos/boogie/raw/boogie-core/docs/hobtt2-2.png?at=refs%2Fheads%2Fmain">
<img align="float: left;" height="500" src="https://mustache.mitre.org/projects/TTFS/repos/boogie/raw/boogie-core/docs/hobtt2-2.png?at=refs%2Fheads%2Fmain"/>
</a>

```java
Procedure hobtt2 = ...;

QueryableProcedure queryable = ProcedureFactory.newQueryableProcedure(hobtt2);
ProcedureGraph graph = ProcedureFactory.newProcedureGraph(hobtt2);

Leg ennttIF = queryable.firstLegOfTransition("ENNTT");
Leg keavyFM = queryable.lastLegOfTransition("RW28");

// we know from the plate there is only one - there can be multiple depending on procedure geometry (though generally there will 
// only be one) 
List<Leg> pathBetween = graph.pathsBetween(ennttIF, keavyFM).get(0);

// applying a simple string transform would return something like...
Function<Leg, String> legFormatter = leg -> ...;
String legSequence = pathBetween.stream().map(legFormatter).collect(Collectors.joining("->"));

assertEquals("ENNTT:IF->GONDR:TF->SMAWG:TF->SMAWG:IF->HOBTT:TF->ENSLL:TF->ENSLL:IF->RAIIN:TF->KLOWD:TF->SWEPT:TF->KYMMY:TF->KEAVY:TF->KEAVY:FM", legSequence);
```

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