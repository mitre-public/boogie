package org.mitre.tdp.boogie.arinc.database;

import static java.util.Collections.emptyList;
import static java.util.Objects.nonNull;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.arinc.model.*;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

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

  public static ArincFixDatabase emptyFixDatabase() {
    return new ArincFixDatabase(LinkedHashMultimap.create());
  }

  /**
   * Factory method for generating a new {@link ArincFixDatabase} which indexes various fix-like ARINC data models by:
   * <br>
   * 1. Identifier
   * 2. ICAO Region
   * 3. Section
   * 4. SubSection (if present for the record type)
   */
  public static ArincFixDatabase newFixDatabase(
      Collection<ArincNdbNavaid> ndbNavaids,
      Collection<ArincVhfNavaid> vhfNavaids,
      Collection<ArincWaypoint> waypoints,
      Collection<ArincAirport> airports,
      Collection<ArincHoldingPattern> holdingPatterns) {

    LinkedHashMultimap<ArincKey, Object> lookup = LinkedHashMultimap.create();

    LOG.debug("Indexing {} NDB Navaids in the FixDatabase.", ndbNavaids.size());
    ndbNavaids.forEach(ndb -> lookup.put(ndbNavaidToFixIndex.apply(ndb), ndb));

    LOG.debug("Indexing {} VHF Navaids in the FixDatabase.", vhfNavaids.size());
    vhfNavaids.forEach(vhf -> lookup.put(vhfNavaidToFixIndex.apply(vhf), vhf));

    LOG.debug("Indexing {} Waypoints in the FixDatabase.", waypoints.size());
    waypoints.forEach(waypoint -> lookup.put(waypointToFixIndex.apply(waypoint), waypoint));

    LOG.debug("Indexing {} Airports in the FixDatabase.", airports.size());
    airports.forEach(airport -> lookup.put(airportToFixIndex.apply(airport), airport));

    LOG.debug("Indexing {} Holding Patterns in the FixDatabase.", airports.size());
    holdingPatterns.forEach(arincHoldingPattern -> lookup.put(holdingToHoldingIndex.apply(arincHoldingPattern), arincHoldingPattern));

    return new ArincFixDatabase(lookup);
  }

  public static ArincTerminalAreaDatabase emptyTerminalAreaDatabase() {
    return new ArincTerminalAreaDatabase(LinkedHashMultimap.create());
  }

  /**
   * Factory method for generating a new {@link ArincTerminalAreaDatabase} which indexes various ARINC records native to Airports by
   * their associated airport identifier and ICAO region.
   */
  public static ArincTerminalAreaDatabase newTerminalAreaDatabase(
      Collection<ArincAirport> airports,
      Collection<ArincRunway> runways,
      Collection<ArincLocalizerGlideSlope> localizerGlideSlopes,
      Collection<ArincNdbNavaid> ndbNavaids,
      Collection<ArincVhfNavaid> vhfNavaids,
      Collection<ArincWaypoint> waypoints,
      Collection<ArincProcedureLeg> procedureLegs,
      Collection<ArincGnssLandingSystem> gnssLandingSystems,
      Collection<ArincHelipad> helipads) {

    LinkedHashMultimap<Pair<String, String>, AirportPage> lookup = LinkedHashMultimap.create();

    Map<Pair<String, String>, List<ArincRunway>> runwayMap = runways.stream().filter(i -> SectionCode.P.equals(i.sectionCode())).collect(Collectors.groupingBy(runwayToAirportIndex));
    Map<Pair<String, String>, List<ArincLocalizerGlideSlope>> localizerMap = localizerGlideSlopes.stream().filter(i -> SectionCode.P.equals(i.sectionCode())).collect(Collectors.groupingBy(localizerGlideSlopeToAirportIndex));
    Map<Pair<String, String>, List<ArincNdbNavaid>> ndbNavaidMap = ndbNavaids.stream().filter(i -> SectionCode.P.equals(i.sectionCode())).collect(Collectors.groupingBy(ndbNavaidToAirportIndex));
    Map<Pair<String, String>, List<ArincVhfNavaid>> vhfNavaidMap = vhfNavaids.stream().collect(Collectors.groupingBy(vhfNavaidToAirportIndex));
    Map<Pair<String, String>, List<ArincWaypoint>> waypointMap = waypoints.stream().filter(i -> SectionCode.P.equals(i.sectionCode())).collect(Collectors.groupingBy(waypointToAirportIndex));
    Map<Pair<String, String>, List<ArincProcedureLeg>> procedureLegMap = procedureLegs.stream().filter(i -> SectionCode.P.equals(i.sectionCode())).collect(Collectors.groupingBy(procedureLegToAirportIndex));
    Map<Pair<String, String>, List<ArincGnssLandingSystem>> gnssLandingSystemMap = gnssLandingSystems.stream().filter(i -> SectionCode.P.equals(i.sectionCode())).collect(Collectors.groupingBy(gnssLandingSystemToAirportIndex));
    Map<Pair<String, String>, List<ArincHelipad>> helipadMap = helipads.stream().filter(i -> SectionCode.P.equals(i.sectionCode())).collect(Collectors.groupingBy(helipadToAirportIndex));
    //airport and region for gls
    airports.forEach(airport -> {
      Pair<String, String> index = airportToAirportIndex.apply(airport); //name and region

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

      Multimap<String, ArincGnssLandingSystem> gm = Multimaps.index(gnssLandingSystemMap.getOrDefault(index, emptyList()), ArincGnssLandingSystem::runwayIdentifier);

      Map<String, RunwayPage> rm = new HashMap<>();
      runwayMap.getOrDefault(index, emptyList()).forEach(runway -> {

        //data publication issues makes relying on the fields in the runways alone not enough for lots of records
        List<ArincLocalizerGlideSlope> allRywLoc = lgm.entrySet().stream()
            .filter(k -> k.getKey().first().equals(runway.runwayIdentifier()))
            .map(Map.Entry::getValue)
            .toList();

        ArincLocalizerGlideSlope primary = runway.ilsMlsGlsIdentifier().map(i -> lgm.get(Pair.of(runway.runwayIdentifier(), i)))
            .or(() -> allRywLoc.stream().findFirst())
            .orElse(null);
        ArincLocalizerGlideSlope secondary = runway.secondaryIlsMlsGlsIdentifier().map(i -> lgm.get(Pair.of(runway.runwayIdentifier(), i)))
            .or(() -> allRywLoc.stream().skip(1).findFirst())
            .filter(i -> nonNull(primary) && !primary.localizerIdentifier().equals(i.localizerIdentifier()))
            .orElse(null);

        RunwayPage runwayPage = new RunwayPage(
            runway,
            primary,
            secondary,
            gm.get(runway.runwayIdentifier())
        );

        rm.putIfAbsent(runway.runwayIdentifier(), runwayPage);
      });

      Map<String, HelipadPage> hm = new HashMap<>();
      //not great but needed because they can have landing systems and for some reason pads don't reference they systems
      helipadMap.getOrDefault(index, emptyList()).forEach(helipad -> {
        List<ArincLocalizerGlideSlope> locs = localizerMap.getOrDefault(Pair.of(helipad.airportHeliportIdentifier(), helipad.icaoCode()), emptyList()).stream()
            .filter(i -> i.runwayIdentifier().equals(helipad.helipadIdentifier()))
            .toList();
        ArincLocalizerGlideSlope primary = locs.stream().findFirst().orElse(null);
        ArincLocalizerGlideSlope secondary = locs.stream().skip(1).findFirst().orElse(null);
        List<ArincGnssLandingSystem> gls = gnssLandingSystemMap.getOrDefault(Pair.of(helipad.airportHeliportIdentifier(), helipad.icaoCode()), emptyList()).stream()
            .filter(i -> i.runwayIdentifier().equals(helipad.helipadIdentifier()))
            .toList();
        HelipadPage page = new HelipadPage(
            helipad,
            primary,
            secondary,
            gls
        );
        hm.putIfAbsent(helipad.helipadIdentifier(), page);
      });

      AirportPage airportPage = new AirportPage(airport, rm, plm, wm, nm, vm, hm);

      lookup.put(index, airportPage);
    });

    return new ArincTerminalAreaDatabase(lookup);
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

  private static final Function<ArincHelipad, Pair<String, String>> helipadToAirportIndex = helipad -> Pair.of(
      helipad.airportHeliportIdentifier(),
      helipad.icaoCode()
  );

  private static final Function<ArincLocalizerGlideSlope, Pair<String, String>> localizerGlideSlopeToAirportIndex = arincLocalizerGlideSlope -> Pair.of(
      arincLocalizerGlideSlope.airportIdentifier(),
      arincLocalizerGlideSlope.airportIcaoRegion()
  );

  private static final Function<ArincGnssLandingSystem, Pair<String, String>> gnssLandingSystemToAirportIndex = arincGnssLandingSystem -> Pair.of(
      arincGnssLandingSystem.airportIdentifier(),
      arincGnssLandingSystem.airportIcaoRegion()
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
      arincNdbNavaid.subSectionCode().orElse(null)
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
      arincAirport.subSectionCode().orElse(null)
  );

  private static final Function<ArincHoldingPattern, ArincKey> holdingToHoldingIndex = arincHoldingPattern -> new ArincKey(
      arincHoldingPattern.fixIdentifier(),
      arincHoldingPattern.fixIcaoRegion(),
      arincHoldingPattern.sectionCode(),
      arincHoldingPattern.subSectionCode()
  );

  private static final Function<ArincHelipad, ArincKey> helipadToHelipadIndex = arincHelipad -> new ArincKey(
      arincHelipad.helipadIdentifier(),
      arincHelipad.icaoCode(),
      arincHelipad.sectionCode(),
      arincHelipad.subSectionCode().orElse(null)
  );
}
