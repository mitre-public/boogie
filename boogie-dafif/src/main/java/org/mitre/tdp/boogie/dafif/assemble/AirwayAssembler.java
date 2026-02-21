package org.mitre.tdp.boogie.dafif.assemble;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.dafif.database.DafifFixDatabase;
import org.mitre.tdp.boogie.dafif.model.DafifAirTrafficSegment;

/**
 * Assembler class for converting collections of {@link DafifAirTrafficSegment} records into a client-defined output class of
 * type {@code A} representing an Airway.
 *
 * <p>DAFIF codes airways with separate records per direction (e.g., direction "E" and "W" for the same airway identifier).
 * Each direction is assembled into its own {@code A} instance. Segments within a direction are sorted by sequence number
 * and produce an N+1 leg chain: the first leg carries the starting fix, subsequent legs carry each segment's ending fix.
 *
 * @param <A> the airway type
 */
public interface AirwayAssembler<A> {

  static AirwayAssembler<Airway> standard(DafifFixDatabase fixDatabase, FixAssemblyStrategy<Fix> fixStrategy) {
    return withStrategy(fixDatabase, fixStrategy, AirwayAssemblyStrategy.standard());
  }

  static <A, F, L> AirwayAssembler<A> withStrategy(DafifFixDatabase fixDatabase, FixAssemblyStrategy<F> fixStrategy, AirwayAssemblyStrategy<A, F, L> airwayStrategy) {
    return new Standard<>(fixDatabase, fixStrategy, airwayStrategy);
  }

  Stream<A> assemble(Collection<DafifAirTrafficSegment> segments);

  final class Standard<A, F, L> implements AirwayAssembler<A> {

    private final DafifFixDatabase fixDatabase;
    private final FixAssemblyStrategy<F> fixStrategy;
    private final AirwayAssemblyStrategy<A, F, L> airwayStrategy;

    private Standard(DafifFixDatabase fixDatabase, FixAssemblyStrategy<F> fixStrategy, AirwayAssemblyStrategy<A, F, L> airwayStrategy) {
      this.fixDatabase = requireNonNull(fixDatabase);
      this.fixStrategy = requireNonNull(fixStrategy);
      this.airwayStrategy = requireNonNull(airwayStrategy);
    }

    @Override
    public Stream<A> assemble(Collection<DafifAirTrafficSegment> segments) {
      Map<String, List<DafifAirTrafficSegment>> byIdentifierAndDirection = segments.stream()
          .collect(Collectors.groupingBy(seg -> seg.atsIdentifier() + "|" + seg.atsRouteDirection()));

      return byIdentifierAndDirection.values().stream().map(this::toAirway);
    }

    private A toAirway(List<DafifAirTrafficSegment> directionSegments) {
      List<DafifAirTrafficSegment> sorted = directionSegments.stream()
          .sorted(Comparator.comparing(DafifAirTrafficSegment::atsRouteSequenceNumber))
          .collect(Collectors.toList());

      List<L> legs = buildLegs(sorted);
      return airwayStrategy.convertAirway(sorted.get(0), legs);
    }

    /**
     * Builds the N+1 leg list from sorted segments within a single direction.
     * The first leg carries the starting fix (wpt1 of first segment), subsequent legs carry each segment's ending fix (wpt2).
     */
    private List<L> buildLegs(List<DafifAirTrafficSegment> sorted) {
      List<L> legs = new ArrayList<>(sorted.size() + 1);

      DafifAirTrafficSegment first = sorted.get(0);
      F startFix = resolveFix(first.waypoint1AtsWaypointDescriptionCode1(),
          first.waypoint1WaypointIdentifierWptIdent(), first.waypoint1CountryCode());
      legs.add(airwayStrategy.convertStartLeg(first, startFix));

      for (DafifAirTrafficSegment seg : sorted) {
        F toFix = resolveFix(seg.waypoint2AtsWaypointDescriptionCode1(),
            seg.waypoint2WaypointIdentifierWptIdent(), seg.waypoint2CountryCode());
        legs.add(airwayStrategy.convertLeg(seg, toFix));
      }

      return legs;
    }

    private F resolveFix(String waypointDescCode, String waypointIdent, String countryCode) {
      return switch (waypointDescCode) {
        case "E", "R" -> fixDatabase.waypoint(waypointIdent, countryCode)
            .map(w -> fixStrategy.convertWaypoint(w).stream())
            .flatMap(Stream::findFirst)
            .orElse(null);
        case "N", "V" -> fixDatabase.waypoint(waypointIdent, countryCode)
            .flatMap(fixDatabase::navaidFor)
            .map(n -> fixStrategy.convertNavaid(n).stream())
            .flatMap(Stream::findFirst)
            .orElse(null);
        default -> fixDatabase.waypoint(waypointIdent, countryCode)
            .map(w -> fixStrategy.convertWaypoint(w).stream())
            .flatMap(Stream::findFirst)
            .orElse(null);
      };
    }
  }
}
