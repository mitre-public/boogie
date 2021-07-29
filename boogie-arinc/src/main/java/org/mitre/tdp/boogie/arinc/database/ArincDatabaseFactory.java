package org.mitre.tdp.boogie.arinc.database;

import static java.util.Collections.emptyList;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.arinc.model.ArincAirport;
import org.mitre.tdp.boogie.arinc.model.ArincLocalizerGlideSlope;
import org.mitre.tdp.boogie.arinc.model.ArincNdbNavaid;
import org.mitre.tdp.boogie.arinc.model.ArincProcedureLeg;
import org.mitre.tdp.boogie.arinc.model.ArincRunway;
import org.mitre.tdp.boogie.arinc.model.ArincVhfNavaid;
import org.mitre.tdp.boogie.arinc.model.ArincWaypoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.LinkedHashMultimap;

/**
 * Factory class for generating a variety of multi-index databases over ARINC information.
 * <br>
 * These database instantiations rely on the format of the converted ARINC POJO data models and do <i>not</i> explicitly stitch
 * records together (e.g. marrying the appropriate fix to a procedure leg). However these databases can be used to aid in those
 * lookups.
 */
public final class ArincDatabaseFactory {

  private static final Logger LOG = LoggerFactory.getLogger(ArincDatabaseFactory.class);

  private ArincDatabaseFactory() {
    throw new IllegalStateException("Cannot instantiate static factory class.");
  }

  /**
   * Factory method for generating a new {@link FixDatabase} which indexes various fix-like ARINC data models by:
   * <br>
   * 1. Identifier
   * 2. ICAO Region
   * 3. Section
   * 4. SubSection (if present for the record type)
   */
  public static FixDatabase newFixDatabase(
      Collection<ArincNdbNavaid> ndbNavaids,
      Collection<ArincVhfNavaid> vhfNavaids,
      Collection<ArincWaypoint> waypoints,
      Collection<ArincAirport> airports) {

    LinkedHashMultimap<ArincKey, Object> lookup = LinkedHashMultimap.create();

    LOG.info("Indexing {} NDB Navaids in the FixDatabase.", ndbNavaids.size());
    ndbNavaids.forEach(ndb -> lookup.put(ndbNavaidToFixIndex.apply(ndb), ndb));

    LOG.info("Indexing {} VHF Navaids in the FixDatabase.", vhfNavaids.size());
    vhfNavaids.forEach(vhf -> lookup.put(vhfNavaidToFixIndex.apply(vhf), vhf));

    LOG.info("Indexing {} Waypoints in the FixDatabase.", waypoints.size());
    waypoints.forEach(waypoint -> lookup.put(waypointToFixIndex.apply(waypoint), waypoint));

    LOG.info("Indexing {} Airports in the FixDatabase.", airports.size());
    airports.forEach(airport -> lookup.put(airportToFixIndex.apply(airport), airport));

    return new FixDatabase(lookup);
  }

  /**
   * Factory method for generating a new {@link TerminalAreaDatabase} which indexes various ARINC records native to Airports by
   * their associated airport identifier and ICAO region.
   */
  public static TerminalAreaDatabase newTerminalAreaDatabase(
      Collection<ArincAirport> airports,
      Collection<ArincRunway> runways,
      Collection<ArincLocalizerGlideSlope> localizerGlideSlopes,
      Collection<ArincNdbNavaid> ndbNavaids,
      Collection<ArincVhfNavaid> vhfNavaids,
      Collection<ArincWaypoint> waypoints,
      Collection<ArincProcedureLeg> procedureLegs) {

    LinkedHashMultimap<Pair<String, String>, AirportPage> lookup = LinkedHashMultimap.create();

    Map<Pair<String, String>, List<ArincRunway>> runwayMap = runways.stream().collect(Collectors.groupingBy(runwayToAirportIndex));
    Map<Pair<String, String>, List<ArincLocalizerGlideSlope>> localizerMap = localizerGlideSlopes.stream().collect(Collectors.groupingBy(localizerGlideSlopeToAirportIndex));
    Map<Pair<String, String>, List<ArincNdbNavaid>> ndbNavaidMap = ndbNavaids.stream().collect(Collectors.groupingBy(ndbNavaidToAirportIndex));
    Map<Pair<String, String>, List<ArincVhfNavaid>> vhfNavaidMap = vhfNavaids.stream().collect(Collectors.groupingBy(vhfNavaidToAirportIndex));
    Map<Pair<String, String>, List<ArincWaypoint>> waypointMap = waypoints.stream().collect(Collectors.groupingBy(waypointToAirportIndex));
    Map<Pair<String, String>, List<ArincProcedureLeg>> procedureLegMap = procedureLegs.stream().collect(Collectors.groupingBy(procedureLegToAirportIndex));

    airports.forEach(airport -> {
      Pair<String, String> index = airportToAirportIndex.apply(airport);

      Map<String, List<ArincProcedureLeg>> plm = procedureLegMap.getOrDefault(index, emptyList()).stream()
          .collect(Collectors.groupingBy(ArincProcedureLeg::sidStarIdentifier));

      // apply some nice-to-have sorting to the entries in the internal structure
      plm.keySet().forEach(key -> plm.computeIfPresent(key, (k, list) -> list.stream().sorted(procedureLegComparator).collect(Collectors.toList())));

      Map<String, ArincWaypoint> wm = waypointMap.getOrDefault(index, emptyList()).stream()
          .collect(Collectors.toMap(ArincWaypoint::waypointIdentifier, Function.identity()));

      Map<String, ArincNdbNavaid> nm = ndbNavaidMap.getOrDefault(index, emptyList()).stream()
          .collect(Collectors.toMap(ArincNdbNavaid::ndbIdentifier, Function.identity()));

      Map<String, ArincVhfNavaid> vm = vhfNavaidMap.getOrDefault(index, emptyList()).stream()
          .collect(Collectors.toMap(ArincVhfNavaid::vhfIdentifier, Function.identity()));

      Map<Pair<String, String>, ArincLocalizerGlideSlope> lgm = localizerMap.getOrDefault(index, emptyList()).stream()
          .collect(Collectors.toMap(lgs -> Pair.of(lgs.runwayIdentifier(), lgs.localizerIdentifier()), Function.identity()));

      Map<String, RunwayPage> rm = new HashMap<>();
      runwayMap.getOrDefault(index, emptyList()).forEach(runway -> {

        RunwayPage runwayPage = new RunwayPage(
            runway,
            lgm.get(Pair.of(runway.runwayIdentifier(), runway.ilsMlsGlsIdentifier().orElse(null))),
            lgm.get(Pair.of(runway.runwayIdentifier(), runway.secondaryIlsMlsGlsIdentifier().orElse(null)))
        );

        rm.putIfAbsent(runway.runwayIdentifier(), runwayPage);
      });

      AirportPage airportPage = new AirportPage(airport, rm, plm, wm, nm, vm);

      lookup.put(index, airportPage);
    });

    return new TerminalAreaDatabase(lookup);
  }

  /**
   * Simple sort function so the legs of procedures are returned in approximately the transition-declared order.
   */
  private static final Comparator<ArincProcedureLeg> procedureLegComparator = Comparator.comparing(ArincProcedureLeg::sidStarIdentifier)
      .thenComparing(leg -> leg.transitionIdentifier().orElse(""))
      .thenComparing(ArincProcedureLeg::sequenceNumber);

  private static final Function<ArincAirport, Pair<String, String>> airportToAirportIndex = arincAirport -> Pair.of(
      arincAirport.airportIdentifier(),
      arincAirport.airportIcaoRegion()
  );

  private static final Function<ArincRunway, Pair<String, String>> runwayToAirportIndex = arincRunway -> Pair.of(
      arincRunway.airportIdentifier(),
      arincRunway.airportIcaoRegion()
  );

  private static final Function<ArincLocalizerGlideSlope, Pair<String, String>> localizerGlideSlopeToAirportIndex = arincLocalizerGlideSlope -> Pair.of(
      arincLocalizerGlideSlope.airportIdentifier(),
      arincLocalizerGlideSlope.airportIcaoRegion()
  );

  private static final Function<ArincNdbNavaid, Pair<String, String>> ndbNavaidToAirportIndex = arincNdbNavaid -> Pair.of(
      arincNdbNavaid.airportIdentifier().orElse(null),
      arincNdbNavaid.airportIcaoRegion().orElse(null)
  );

  private static final Function<ArincVhfNavaid, Pair<String, String>> vhfNavaidToAirportIndex = arincVhfNavaid -> Pair.of(
      arincVhfNavaid.airportIdentifier().orElse(null),
      arincVhfNavaid.airportIcaoRegion().orElse(null)
  );

  private static final Function<ArincWaypoint, Pair<String, String>> waypointToAirportIndex = arincWaypoint -> Pair.of(
      arincWaypoint.airportIdentifier().orElse(null),
      arincWaypoint.airportIcaoRegion().orElse(null)
  );

  private static final Function<ArincProcedureLeg, Pair<String, String>> procedureLegToAirportIndex = arincProcedureLeg -> Pair.of(
      arincProcedureLeg.airportIdentifier(),
      arincProcedureLeg.airportIcaoRegion()
  );

  private static final Function<ArincNdbNavaid, ArincKey> ndbNavaidToFixIndex = arincNdbNavaid -> new ArincKey(
      arincNdbNavaid.ndbIdentifier(),
      arincNdbNavaid.ndbIcaoRegion(),
      arincNdbNavaid.sectionCode(),
      arincNdbNavaid.subSectionCode()
  );

  private static final Function<ArincVhfNavaid, ArincKey> vhfNavaidToFixIndex = arincVhfNavaid -> new ArincKey(
      arincVhfNavaid.vhfIdentifier(),
      arincVhfNavaid.vhfIcaoRegion(),
      arincVhfNavaid.sectionCode(),
      arincVhfNavaid.subSectionCode().orElse(null)
  );

  private static final Function<ArincWaypoint, ArincKey> waypointToFixIndex = arincWaypoint -> new ArincKey(
      arincWaypoint.waypointIdentifier(),
      arincWaypoint.waypointIcaoRegion(),
      arincWaypoint.sectionCode(),
      arincWaypoint.terminalSubSectionCode().orElse(arincWaypoint.enrouteSubSectionCode().orElse(null))
  );

  private static final Function<ArincAirport, ArincKey> airportToFixIndex = arincAirport -> new ArincKey(
      arincAirport.airportIdentifier(),
      arincAirport.airportIcaoRegion(),
      arincAirport.sectionCode(),
      arincAirport.subSectionCode()
  );
}
