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

## Updating World Magnetic Model coefficients

Every addition or update to `GeomagneticCoefficients` must include numerical regression tests against published NOAA/BGS
reference values for that model. Changes to the geomagnetic calculation must also pass these tests before merging.
Constructor checks alone are insufficient: a missing metadata row can silently use the first coefficient as the model epoch
and skip that coefficient without throwing an exception.

Use [TestGeomagnetics](src/test/java/org/mitre/tdp/boogie/TestGeomagnetics.java) and the
[WMM2025 reference fixture](src/test/resources/geomagnetics/wmm2025-reference.csv) as examples. The fixture comes from
[NOAA's published WMM2025 test values](https://www.ncei.noaa.gov/sites/default/files/2025-02/WMM2025testvalues.pdf).

When updating a model:

1. Preserve the metadata row at index zero, with the model epoch as its first value, followed by all coefficient rows.
2. Add reference values for the new model under `src/test/resources/geomagnetics/`, recording the source URL, retrieval date,
   and units. Expected values must come from the published reference, not from this implementation. Keep existing model tests.
3. Compare all seven outputs: declination, inclination, and north, east, vertical, horizontal, and total field intensity.
   Cover multiple coordinates, dates (including a date after the epoch), and heights. Use decimal years, angles in degrees,
   and heights in kilometers above the WGS84 ellipsoid when calling `Geomagnetics`; field intensities are in nanoteslas.
4. Check the default overloads against reference values at the model epoch plus 2.5 years and zero height.
   Match tolerances to the reference's published precision: the WMM2025 tests use 0.01 degrees and 0.1 nT.
   Investigate mismatches instead of widening tolerances to make a failing test pass.
5. Run the utility test suite from the repository root and require it to pass before merging:

```sh
./gradlew :boogie-util:test
```

The reference fixtures are checked into the repository, so these tests do not fetch data from NOAA when they run.
