# boogie-airspace

Compile Boogie `Airspace` boundaries once, then find every occupied interval along a trajectory.
The module uses spherical geometry and a spatial index, and returns caller keys and fractions
without exposing S2 classes. It supports use in standalone applications and distributed workers.

```java
import java.util.List;

import org.mitre.tdp.boogie.airspace.AirspaceDefinition;
import org.mitre.tdp.boogie.airspace.AirspaceIndex;
import org.mitre.tdp.boogie.airspace.AirspaceIndexFactory;
import org.mitre.tdp.boogie.airspace.AirspaceQuery;
import org.mitre.tdp.boogie.airspace.AltitudeBand;

// Use a stable key that identifies the airspace, source, and layer.
var definitions = List.of(
    new AirspaceDefinition<>("source-key:FIR", fir),
    new AirspaceDefinition<>("source-key:UIR", uir, AltitudeBand.atLeast(18000.0)));

// Line samples every 5 NM; arc/circle bearings every 1 degree. The default factory uses 10 NM / 10 degrees.
var factory = new AirspaceIndexFactory<String>(5.0, 1.0);
AirspaceIndex<String> index = factory.apply(definitions);

// Inspect incomplete altitude definitions during initialization.
var unresolvedKeys = index.unknownAltitudeKeys();

// Supply the consumer once and reuse the query on one processing thread.
AirspaceQuery<String> query = index.newQuery(span -> {
  // Fraction u maps to time as t0 + u * (t1 - t0).
  consume(span.airspaceKey(), span.enterFraction(), span.exitFraction(), span.altitudeChecked());
});
query.intersect(startLatLong, endLatLong);

// Optional vertical clipping: heights and limits must share an altitude reference, in feet.
query.intersect(startLatLong, startAltitudeFeet, endLatLong, endAltitudeFeet);

// Track observations may lack altitude at either endpoint.
query.intersect(startLatLong, startAltitudeFeet, endLatLong, null);
```

## Joining spans into visits

`AirspaceVisitAccumulator<K, E>` combines spans for consecutive observation pairs into complete visits.
Create one accumulator for a trajectory and supply a function mapping your event type to an
`AirspacePoint(time, position, altitudeFeet)`. Altitude remains nullable. The accumulator retains the
original events covered by each visit and emits an `AirspaceVisit` when its coverage ends.

```java
List<AirspaceSpan<String>> spans = new ArrayList<>();
AirspaceQuery<String> query = index.newQuery(spans::add);
var visits = new AirspaceVisitAccumulator<String, TrackEvent>(
    event -> new AirspacePoint(event.time(), event.position(), event.altitudeFeet()),
    visit -> consumeVisit(visit));

for (int i = 1; i < events.size(); i++) {
  TrackEvent start = events.get(i - 1);
  TrackEvent end = events.get(i);
  spans.clear();
  query.intersect(start.position(), start.altitudeFeet(), end.position(), end.altitudeFeet());
  visits.accept(start, end, spans); // Include legs with no spans.
}
visits.finish(); // Emit visits still open at the end of this trajectory and reset state.
```

`TrackEvent`, `events`, and `consumeVisit` above belong to the calling application. The snippet uses
`java.util.ArrayList` and the module's `AirspacePoint`, `AirspaceSpan`, and `AirspaceVisitAccumulator`.
The query and accumulator can both be reused after finishing a trajectory, on the same processing thread.

For an airspace crossed through observations A, B, C, D, the spans might be:

| Leg | Enter fraction | Exit fraction |
| --- | --- | --- |
| A → B | 0.4 | 1.0 |
| B → C | 0.0 | 1.0 |
| C → D | 0.0 | 0.3 |

These form one visit containing B and C, with interpolated entry between A/B and exit between C/D.
`visit.duration()` gives the time from `visit.entry().time()` to `visit.exit().time()`.

- Join across legs only when the mapped shared observation is equal and the spans touch at `1 → 0`.
  Within one leg, touching spans also join; disjoint spans produce separate visits. Each airspace key
  is tracked independently, so overlapping airspaces retain separate visits.
- `AirspacePointInterpolator` computes entry/exit positions along the shortest great circle and times
  linearly. Interior altitude is interpolated only when both heights are present. Exact endpoint
  fractions preserve the original endpoint's available values.
- `entryCrossing()` and `exitCrossing()` are true when that transition falls inside a supplied leg.
  A false flag means the endpoint marks available coverage, which can include a track starting or
  ending inside, a data gap, or a query with unresolved vertical coverage.
- `altitudeChecked()` is true only if every contributing span had altitude checked. A visit containing
  lateral-only spans does not establish definite vertical membership for its entire duration.
- Call `accept` even for an empty result, and call `finish()` between trajectories or before skipping
  an untrusted gap. The accumulator does not choose a maximum time gap. Events must advance in time.
- Span lists can be cleared after each call. Completed visits contain an immutable copy of their
  original-event list. Only active visits remain in the accumulator, so its memory depends on the
  duration of open visits and the number of overlapping airspaces, rather than completed visits.

## Missing track altitude

Altitude inputs are nullable `Double`s; pass `null` for a missing observation altitude. When both endpoints have altitude,
the query clips against the airspace's altitude band and emits spans with `altitudeChecked() == true`.
If either endpoint lacks altitude, it emits lateral overlaps with `altitudeChecked() == false`.
The query does not fill missing heights or extrapolate from a single observation. Present altitudes
must be finite; use `null` for missing data.

The two-position overload also emits lateral spans with `altitudeChecked() == false`. These spans
retain potential airspace visits through gaps in altitude data; they do not confirm vertical membership.

Keep separate keys for layers with different footprints or altitude bands. A later altitude match in
one layer does not confirm earlier unchecked spans in that layer or in another overlapping layer.
Even closely spaced observations can cross a change in the airspace floor. A visit containing an
unchecked span remains unchecked when subsequent spans have known altitudes.

## Altitude limits

The factory uses `airspace.altitudeLimit()` when an `AirspaceDefinition` has no altitude override:

| Available limits | Interpretation |
| --- | --- |
| Lower and upper | Within the inclusive band |
| Upper only | Everything at or below the upper limit |
| Lower only | Everything at or above the lower limit |
| Neither, or no range | Unknown vertical band; listed by `unknownAltitudeKeys()` |

One missing bound leaves that side unlimited. A missing lower limit does not impose a zero-foot MSL
floor; a ground-based airspace with an upper limit accepts airborne observations below that ceiling.
Both missing limits need attention and do not silently make the airspace unlimited.
`AltitudeBand.of(Double, Double)` provides the same mapping for adapters, using `null` for missing limits.

An explicit band supplied to `new AirspaceDefinition<>(key, airspace, band)` overrides the generic limits. Use
`AltitudeBand.unbounded()` when intentionally applying no vertical constraint, or `AltitudeBand.unknown()`
when an altitude reference remains unresolved. Lateral queries include unknown bands; vertical queries
omit them. Generic altitude ranges must have inclusive endpoints.

Callers normalize numeric heights to a common reference before vertical queries. This library does not
convert AGL, pressure altitude, or flight levels to MSL, and it does not resolve terrain or airspace activity.

## Geometry and result contract

- Sequences are sorted by sequence number. Each sequence's geometry describes its associated fix
  to the next fix; the last sequence closes to the first. A circle is a single sequence with center/radius.
- The existing projection helpers accept configurable steps: nautical miles for great-circle/rhumb lines,
  degrees for arcs/circles. `project10NM` and `project10Deg` delegate to the general `project` methods.
  Smaller steps produce more samples; step sizes do not promise a geometric error bound. Samples include
  each edge's start and exclude its end, which is supplied by the next sequence.
- One simple ring is supported per airspace. Winding order is ignored and the smaller spherical region
  is selected. Holes, disconnected rings, and regions larger than a hemisphere require separate modeling.
  Circles of a hemisphere or larger are rejected. Invalid/self-crossing boundaries and duplicate keys or
  sequence numbers fail compilation with the caller key in the error.
- Arc bearings come from the center and endpoint fixes, and intermediate samples use the radius from the
  center to the starting fix. The next sequence supplies the published endpoint even when its radius differs.
- Each trajectory leg follows the shortest great circle; antipodal endpoints are rejected. Fractions
  refer to progress along that original leg. Altitude and time are assumed linear in those fractions.
  Stationary positions still support vertical entries/exits.
- All positive-length occupied intervals are returned, including an outside-to-outside traversal and
  multiple visits through a concave boundary. Isolated touches emit no span. Exact boundary ownership
  follows [S2's semi-open convention](https://github.com/google/s2-geometry-library-java).
- A zero entry fraction means occupancy starts at the first observation; it does not establish an observed
  entry event. Results follow input collection iteration order by airspace and travel order within each airspace. Overlapping
  airspaces retain their independent keys and spans.

## Application integration

Use `AirspaceIndexFactory.apply(...)` when loading an airspace dataset, then reuse the index across trajectories.
Definitions hold source data; the factory validates inputs, resolves altitude bands, compiles boundaries, and
prepares the spatial index. Each application of the factory returns an independent snapshot. The index, definition,
and factory constructors only store their inputs and check required references. Use an ordered input collection
for predictable result order.

Candidate selection combines endpoint containment with boundary-edge candidates, so it also finds brief visits between outside
observations and vertical entries while both lateral endpoints remain inside. Geometry compilation and index
construction are outside the observation loop. Supply the result consumer when creating the query; every
intersection call invokes that consumer synchronously. Use separate query objects per thread; callbacks
cannot recursively invoke the same query.

The calling application supplies airspace keys, selects the source dataset, normalizes altitude limits,
and passes consecutive trajectory observations to the query. The visit accumulator joins intervals and
interpolates their endpoints. The application handles unusable points, gaps, activity schedules, and
excursion policy. A missing span from an unknown vertical band does not establish a
definite outside classification. In distributed processing, each worker can build and reuse its own index.

The module uses the public Maven Central `com.google.geometry:s2-geometry:2.0.0` artifact.

## Validation

```sh
./gradlew :boogie-airspace:test
```

Tests cover crossings, overlapping bands, missing track altitudes, stationary vertical entries, unknown limits, circles, concave
reentry, date-line and polar paths, boundary touches, indexed versus individual queries, and concurrent
queries with separate worker state. Source assembly regressions cover ARINC and DAFIF boundary semantics.
Visit tests cover joining consecutive legs, original-event retention, interpolation, reentries, overlaps,
missing altitude, and incomplete coverage at trajectory boundaries.
