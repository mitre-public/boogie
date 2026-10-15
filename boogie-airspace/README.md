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
- Source endpoint and center coordinates are validated before projection. Arc endpoints must be distinct
  from and non-antipodal to their center so both bearings are defined. Rhumb lines support pole endpoints;
  an opposite-pole edge requires a common longitude to specify its meridian.
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

### Boundary issue identification and resolution

Run repair policies explicitly on ordered source sequences before building an index. The factory and
boundary compiler sort, project, and validate the supplied airspaces. They do not configure or apply
repair policies.

`IssueResolver<T, I>` in `boogie-airspace` holds an identifier (`Function<T, List<I>>`) and a repair
function (`BiFunction<T, List<I>, T>`). `identify(value)` returns typed findings without changing the
value; `resolve(value, issues)` applies the repair function. `apply(value)` identifies once, then
resolves only when findings are present. Policies can be composed with `andThen`, so later detectors
inspect earlier repairs. Both boundary resolvers supply their identifier and repair function to this
shared implementation. Callers can build other policies with
`IssueResolver.of(identifier, resolver)`.

```java
import java.util.Comparator;
import java.util.List;
import org.mitre.tdp.boogie.AirspaceSequence;
import org.mitre.tdp.boogie.airspace.BoundaryRetraceResolver;

// Remove exact out-and-back excursions up to 1 NM each way. The application chooses this limit.
var retraces = new BoundaryRetraceResolver(1.0);

// Given an Airspace.Standard source, preserve its metadata while replacing the boundary.
List<AirspaceSequence> orderedSequences = List.copyOf(source.sequences().stream()
    .sorted(Comparator.comparingInt(AirspaceSequence::sequenceNumber)).toList());
var repaired = source.toBuilder().sequences(retraces.apply(orderedSequences)).build();
var index = new AirspaceIndexFactory<String>()
    .apply(List.of(new AirspaceDefinition<>("my-airspace", repaired)));

// Inspect separately when a caller wants to review findings before applying repairs.
var issues = retraces.identify(orderedSequences);
```

`BoundaryRetraceIdentifier` reports exact A → B → A retraces with their three source sequence numbers
and one-way length. `BoundaryRetraceResolver` combines it with a length-limited removal policy.
Both excursion edges must use the same line geometry (great circle or rhumb line),
and the two A fixes must have exactly equal coordinates. It removes the first A and B records, retaining
the returning A's outgoing geometry, arc parameters, and source metadata. `identify(...)` reports
retraces of any length; `resolve(...)` repairs the first supplied finding within the configured limit
in traversal order. Each call removes one retrace. `apply(...)` repeats inspection and repair until
stable, so nested and overlapping retraces and retraces across the ring closure are handled. Every
successful pass removes records.
Arcs, mixed geometries, near returns, zero-length edges, and antipodal great-circle edges are left alone.
Larger retraces remain in the boundary and fail normal validation if unresolved. This resolver is opt-in.

`BoundaryArcRadiusIdentifier` compares the distance from an arc's center to its own fix with the
distance to the next sequence's fix. It reports the two sequence numbers, both radii, and their
absolute difference when that difference exceeds a caller-supplied tolerance in nautical miles.
Both arc directions and the closing edge are supported, including two-sequence boundaries.
Published radius and bearing fields are ignored, matching projection's coordinate-based calculation.

```java
import org.mitre.tdp.boogie.airspace.BoundaryArcRadiusIdentifier;

// Example tolerance; choose it to account for the source's coordinate rounding.
var arcRadii = new BoundaryArcRadiusIdentifier(0.05);
var mismatches = arcRadii.apply(orderedSequences);
```

This identifier leaves the source unchanged. A mismatch does not establish a crossing or determine
whether the center or an endpoint needs correction. Combine it with `IssueResolver.of(...)` when
the application has chosen a reporting or repair policy. Lines and circles are ignored; missing
coordinates remain subject to normal compilation validation.

`BoundaryArcRadiusResolver` supplies a center-correction policy for those findings. It averages the
two measured radii, intersects equal-radius spherical circles around the endpoint fixes, and chooses
the candidate center nearest the original. Both fixes, arc direction, published arc parameters, and
record source data are preserved. Selected arcs are repaired independently in one pass, including
closing arcs. Coincident or nearly antipodal endpoints are left unchanged. The factory still
validates the resulting polygon; equalizing radii does not guarantee that unrelated crossings disappear.

```java
import org.mitre.tdp.boogie.airspace.BoundaryArcRadiusResolver;

var correctedArcs = new BoundaryArcRadiusResolver(0.05);
var repairedSequences = retraces.andThen(correctedArcs).apply(orderedSequences);
var repaired = source.toBuilder().sequences(repairedSequences).build();
var index = new AirspaceIndexFactory<String>()
    .apply(List.of(new AirspaceDefinition<>("my-airspace", repaired)));
```

Boundary resolvers expect sequences sorted by sequence number, with implicit closure from the last
sequence to the first. They return replacement lists and preserve source objects. The caller rebuilds
the airspace with those sequences and preserves its metadata and any explicit altitude override.
`toBuilder()` is available on `Airspace.Standard`; other source representations require rebuilding
the corresponding airspace while retaining their source records. Re-sort custom policy outputs before
passing them to a resolver that requires traversal order.

Applications can supply custom policies with `IssueResolver.of(identifier, resolver)` and choose
repair limits and reporting. Arc inconsistency policies inspect centers and endpoints before projection.
No geometric repair is enabled automatically. Repaired boundaries still pass sequence, coordinate, and polygon
validation; unresolved crossings fail compilation with the caller's key. Calling a composed repair
function applies each policy in order, so policies needing repeated local repairs manage their own convergence.

### Building and querying an index

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
