# Boogie Util

## Module overview

This module provides some useful, basic calculations for aviation software using navigation data

## Quick start

There are just two classes worth knowing here. AiracCycle and Declinations.

```java

Instant startDate = AiracCycle.startDate("2102");
Instant endDate = AiracCycle.endDate("2101");
String cycleFor = AiracCycle.cycleFor(Instant.now());

double magneticVariation = Declinations.declination(40.0, 40.0, 5000, Instant.now());
```
