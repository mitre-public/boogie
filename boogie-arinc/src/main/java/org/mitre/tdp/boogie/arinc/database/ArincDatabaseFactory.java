package org.mitre.tdp.boogie.arinc.database;

import static java.util.Collections.emptyList;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.arinc.model.ArincAirport;
import org.mitre.tdp.boogie.arinc.model.ArincGnssLandingSystem;
import org.mitre.tdp.boogie.arinc.model.ArincHelipad;
import org.mitre.tdp.boogie.arinc.model.ArincHeliport;
import org.mitre.tdp.boogie.arinc.model.ArincHoldingPattern;
import org.mitre.tdp.boogie.arinc.model.ArincLocalizerGlideSlope;
import org.mitre.tdp.boogie.arinc.model.ArincNdbNavaid;
import org.mitre.tdp.boogie.arinc.model.ArincProcedureLeg;
import org.mitre.tdp.boogie.arinc.model.ArincRunway;
import org.mitre.tdp.boogie.arinc.model.ArincVhfNavaid;
import org.mitre.tdp.boogie.arinc.model.ArincWaypoint;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.LinkedHashMultimap;

/**
 * Factory class for generating a variety of multi-index databases over ARINC information.
 * <p>
 * These database instantiations rely on the format of the converted ARINC POJO data models and do <i>not</i> explicitly stitch
 * records together (e.g. marrying the appropriate fix to a procedure leg). However these databases can be used to aid in those
 * lookups.
 */
public final class ArincDatabaseFactory {

  private static final Logger LOG = LoggerFactory.getLogger(ArincDatabaseFactory.class);
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
  private static final Function<ArincHeliport, Pair<String, String>> heliportToHeliportIndex = arincHeliport -> Pair.of(
      arincHeliport.heliportIdentifier(),
      arincHeliport.heliportIcaoRegion()
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
  private static final Predicate<ArincWaypoint> allWaypoints = waypoint -> true;
  private static final Predicate<ArincWaypoint> enrouteWaypoints = waypoint -> SectionCode.E.equals(waypoint.sectionCode());
  private static final Function<ArincAirport, ArincKey> airportToFixIndex = arincAirport -> new ArincKey(
      arincAirport.airportIdentifier(),
      arincAirport.airportIcaoRegion(),
      arincAirport.sectionCode(),
      arincAirport.subSectionCode().orElse(null)
  );
  private static final Function<ArincHeliport, ArincKey> heliportToFixIndex = arincHeliport -> new ArincKey(
      arincHeliport.heliportIdentifier(),
      arincHeliport.heliportIcaoRegion(),
      arincHeliport.sectionCode(),
      arincHeliport.subSectionCode().orElse(null)
  );
  private static final Function<ArincHoldingPattern, ArincKey> holdingToHoldingIndex = arincHoldingPattern -> new ArincKey(
      arincHoldingPattern.fixIdentifier(),
      arincHoldingPattern.fixIcaoRegion(),
      arincHoldingPattern.sectionCode(),
      arincHoldingPattern.subSectionCode()
  );

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
      Collection<ArincHoldingPattern> holdingPatterns,
      Collection<ArincHeliport> heliports) {

    return newFixDatabase(ndbNavaids, vhfNavaids, waypoints, airports, holdingPatterns, heliports, allWaypoints, true);
  }

  /**
   * Creates the fix database used internally by the OneShot parser.
   *
   * <p>OneShot dereferences fixes with their ICAO region, so this database intentionally omits the duplicate identifier-only
   * indices used by the convenience lookup methods on a standard {@link ArincFixDatabase}. Terminal waypoints and holding
   * patterns are also omitted because OneShot resolves terminal waypoints through its terminal-area database and does not
   * dereference holding patterns.
   */
  public static ArincFixDatabase newOneShotFixDatabase(
      Collection<ArincNdbNavaid> ndbNavaids,
      Collection<ArincVhfNavaid> vhfNavaids,
      Collection<ArincWaypoint> waypoints,
      Collection<ArincAirport> airports,
      Collection<ArincHeliport> heliports) {

    return newFixDatabase(ndbNavaids, vhfNavaids, waypoints, airports, List.of(), heliports, enrouteWaypoints, false);
  }

  private static ArincFixDatabase newFixDatabase(
      Collection<ArincNdbNavaid> ndbNavaids,
      Collection<ArincVhfNavaid> vhfNavaids,
      Collection<ArincWaypoint> waypoints,
      Collection<ArincAirport> airports,
      Collection<ArincHoldingPattern> holdingPatterns,
      Collection<ArincHeliport> heliports,
      Predicate<ArincWaypoint> waypointFilter,
      boolean addIdentifierOnlyIndices) {

    LinkedHashMultimap<ArincKey, Object> lookup = LinkedHashMultimap.create();

    LOG.debug("Indexing {} NDB Navaids in the FixDatabase.", ndbNavaids.size());
    ndbNavaids.forEach(ndb -> lookup.put(ndbNavaidToFixIndex.apply(ndb), ndb));

    LOG.debug("Indexing {} VHF Navaids in the FixDatabase.", vhfNavaids.size());
    vhfNavaids.forEach(vhf -> lookup.put(vhfNavaidToFixIndex.apply(vhf), vhf));

    LOG.debug("Indexing FixDatabase Waypoints from {} candidates.", waypoints.size());
    waypoints.stream()
        .filter(waypointFilter)
        .forEach(waypoint -> lookup.put(waypointToFixIndex.apply(waypoint), waypoint));

    LOG.debug("Indexing {} Airports in the FixDatabase.", airports.size());
    airports.forEach(airport -> lookup.put(airportToFixIndex.apply(airport), airport));

    LOG.debug("Indexing {} Holding Patterns in the FixDatabase.", holdingPatterns.size());
    holdingPatterns.forEach(arincHoldingPattern -> lookup.put(holdingToHoldingIndex.apply(arincHoldingPattern), arincHoldingPattern));

    LOG.debug("Indexing {} Heliports in the FixDatabase.", heliports.size());
    heliports.forEach(arincHeliport -> lookup.put(heliportToFixIndex.apply(arincHeliport), arincHeliport));

    return new ArincFixDatabase(lookup, addIdentifierOnlyIndices);
  }

  public static ArincTerminalAreaDatabase emptyTerminalAreaDatabase() {
    return new ArincTerminalAreaDatabase(LinkedHashMultimap.create(), LinkedHashMultimap.create());
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
      Collection<ArincHelipad> helipads,
      Collection<ArincHeliport> heliports) {

    LinkedHashMultimap<Pair<String, String>, AirportPage> airportLookup = LinkedHashMultimap.create();
    LinkedHashMultimap<Pair<String, String>, HeliportPage> heliportLookup = LinkedHashMultimap.create();

    Map<Pair<String, String>, List<ArincRunway>> runwayMap = groupedBy(runways, runwayToAirportIndex);
    Map<Pair<String, String>, List<ArincLocalizerGlideSlope>> localizerMap = groupedBy(localizerGlideSlopes, localizerGlideSlopeToAirportIndex);
    LOG.debug("Indexing {} NDB Navaids in the ArincTerminalAreaDatabase.", ndbNavaids.size());
    Map<Pair<String, String>, List<ArincNdbNavaid>> ndbNavaidMap = groupedBy(ndbNavaids, ndbNavaidToAirportIndex);
    LOG.debug("Indexing {} VHF Navaids in the ArincTerminalAreaDatabase.", vhfNavaids.size());
    Map<Pair<String, String>, List<ArincVhfNavaid>> vhfNavaidMap = groupedBy(vhfNavaids, vhfNavaidToAirportIndex);
    LOG.debug("Indexing {} Waypoints in the ArincTerminalAreaDatabase.", waypoints.size());
    Map<Pair<String, String>, List<ArincWaypoint>> waypointMap = mapTerminalWaypoints(waypoints);
    LOG.debug("Indexing {} Procedure Legs in the ArincTerminalAreaDatabase.", procedureLegs.size());
    Map<Pair<String, String>, List<ArincProcedureLeg>> procedureLegMap = groupedBy(procedureLegs, procedureLegToAirportIndex);
    LOG.debug("Indexing {} Gnss Landing Systems in the ArincTerminalAreaDatabase.", gnssLandingSystems.size());
    Map<Pair<String, String>, List<ArincGnssLandingSystem>> gnssLandingSystemMap = groupedBy(gnssLandingSystems, gnssLandingSystemToAirportIndex);
    LOG.debug("Indexing {} Helipads in the ArincTerminalAreaDatabase.", helipads.size());
    Map<Pair<String, String>, List<ArincHelipad>> helipadMap = groupedBy(helipads, helipadToAirportIndex);

    LOG.debug("Indexing {} Airports in the ArincTerminalAreaDatabase.", airports.size());
    airports.forEach(airport -> airportPage(runwayMap, localizerMap, ndbNavaidMap, vhfNavaidMap, waypointMap, procedureLegMap, gnssLandingSystemMap, helipadMap, airport, airportLookup));
    LOG.debug("Indexing {} Heliports in the ArincTerminalAreaDatabase.", heliports.size());
    heliports.forEach(heliport -> heliportPage(runwayMap, localizerMap, ndbNavaidMap, vhfNavaidMap, waypointMap, procedureLegMap, gnssLandingSystemMap, helipadMap, heliport, heliportLookup));

    return new ArincTerminalAreaDatabase(airportLookup, heliportLookup);
  }

  private static Map<Pair<String, String>, List<ArincWaypoint>> mapTerminalWaypoints(Collection<ArincWaypoint> waypoints) {
    if (isNull(waypoints) || waypoints.isEmpty()) {
      return Map.of();
    }
    return waypoints.stream()
        .filter(w -> SectionCode.P.equals(w.sectionCode()) || SectionCode.H.equals(w.sectionCode()))
        .collect(Collectors.groupingBy(waypointToAirportIndex));
  }

  /**
   * Creates the terminal-area database used internally by the OneShot parser.
   *
   * <p>Procedure legs are assembled directly by OneShot, while NDB and VHF references are resolved through its fix database.
   * Omitting those three supporting indices avoids retaining duplicate collections without affecting the runway, localizer,
   * landing-system, helipad, or terminal-waypoint lookups used during OneShot assembly.
   */
  public static ArincTerminalAreaDatabase newOneShotTerminalAreaDatabase(
      Collection<ArincAirport> airports,
      Collection<ArincRunway> runways,
      Collection<ArincLocalizerGlideSlope> localizerGlideSlopes,
      Collection<ArincWaypoint> waypoints,
      Collection<ArincGnssLandingSystem> gnssLandingSystems,
      Collection<ArincHelipad> helipads,
      Collection<ArincHeliport> heliports) {

    return newTerminalAreaDatabase(
        airports,
        runways,
        localizerGlideSlopes,
        List.of(),
        List.of(),
        waypoints,
        List.of(),
        gnssLandingSystems,
        helipads,
        heliports
    );
  }

  private static void airportPage(
      Map<Pair<String, String>, List<ArincRunway>> runwayMap,
      Map<Pair<String, String>, List<ArincLocalizerGlideSlope>> localizerMap,
      Map<Pair<String, String>, List<ArincNdbNavaid>> ndbNavaidMap,
      Map<Pair<String, String>, List<ArincVhfNavaid>> vhfNavaidMap,
      Map<Pair<String, String>, List<ArincWaypoint>> waypointMap,
      Map<Pair<String, String>, List<ArincProcedureLeg>> procedureLegMap,
      Map<Pair<String, String>, List<ArincGnssLandingSystem>> gnssLandingSystemMap,
      Map<Pair<String, String>, List<ArincHelipad>> helipadMap,
      ArincAirport airport,
      LinkedHashMultimap<Pair<String, String>, AirportPage> airportLookup
  ) {
    Pair<String, String> index = airportToAirportIndex.apply(airport); //name and region

    Map<String, RunwayPage> rm = runwayPages(runwayMap, localizerMap, gnssLandingSystemMap, index);
    Map<String, HelipadPage> hm = helipadMap(localizerMap, gnssLandingSystemMap, helipadMap, index);
    SupportingPage supportingPage = supportingPage(ndbNavaidMap, vhfNavaidMap, waypointMap, procedureLegMap, index);
    AirportPage airportPage = new AirportPage(airport, rm, hm, supportingPage);
    airportLookup.put(index, airportPage);
  }

  private static void heliportPage(
      Map<Pair<String, String>, List<ArincRunway>> runwayMap,
      Map<Pair<String, String>, List<ArincLocalizerGlideSlope>> localizerMap,
      Map<Pair<String, String>, List<ArincNdbNavaid>> ndbNavaidMap,
      Map<Pair<String, String>, List<ArincVhfNavaid>> vhfNavaidMap,
      Map<Pair<String, String>, List<ArincWaypoint>> waypointMap,
      Map<Pair<String, String>, List<ArincProcedureLeg>> procedureLegMap,
      Map<Pair<String, String>, List<ArincGnssLandingSystem>> gnssLandingSystemMap,
      Map<Pair<String, String>, List<ArincHelipad>> helipadMap,
      ArincHeliport heliport,
      LinkedHashMultimap<Pair<String, String>, HeliportPage> heliportLookup
  ) {
    Pair<String, String> index = heliportToHeliportIndex.apply(heliport); //name and region
    Map<String, RunwayPage> rm = runwayPages(runwayMap, localizerMap, gnssLandingSystemMap, index);
    Map<String, HelipadPage> hm = helipadMap(localizerMap, gnssLandingSystemMap, helipadMap, index);
    SupportingPage supportingPage = supportingPage(ndbNavaidMap, vhfNavaidMap, waypointMap, procedureLegMap, index);
    HeliportPage heliportPage = new HeliportPage(heliport, rm, hm, supportingPage);
    heliportLookup.put(index, heliportPage);
  }

  private static Map<String, RunwayPage> runwayPages(
      Map<Pair<String, String>, List<ArincRunway>> runwayMap,
      Map<Pair<String, String>, List<ArincLocalizerGlideSlope>> localizerMap,
      Map<Pair<String, String>, List<ArincGnssLandingSystem>> gnssLandingSystemMap,
      Pair<String, String> index
  ) {
    List<ArincRunway> runways = runwayMap.getOrDefault(index, emptyList());
    if (runways.isEmpty()) {
      return Map.of();
    }

    List<ArincLocalizerGlideSlope> localizers = localizerMap.getOrDefault(index, emptyList());
    Map<Pair<String, String>, ArincLocalizerGlideSlope> lgm = localizers.isEmpty()
        ? Map.of()
        : localizers.stream().collect(Collectors.toMap(lgs -> Pair.of(lgs.runwayIdentifier(), lgs.localizerIdentifier()), Function.identity()));

    List<ArincGnssLandingSystem> landingSystems = gnssLandingSystemMap.getOrDefault(index, emptyList());
    Map<String, List<ArincGnssLandingSystem>> gm = landingSystems.isEmpty()
        ? Map.of()
        : landingSystems.stream().collect(Collectors.groupingBy(ArincGnssLandingSystem::runwayIdentifier));

    Map<String, RunwayPage> rm = new HashMap<>();
    runways.forEach(runway -> {

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
          gm.getOrDefault(runway.runwayIdentifier(), emptyList())
      );

      rm.putIfAbsent(runway.runwayIdentifier(), runwayPage);
    });
    return rm;
  }

  private static Map<String, HelipadPage> helipadMap(Map<Pair<String, String>, List<ArincLocalizerGlideSlope>> localizerMap, Map<Pair<String, String>, List<ArincGnssLandingSystem>> gnssLandingSystemMap, Map<Pair<String, String>, List<ArincHelipad>> helipadMap, Pair<String, String> index) {
    List<ArincHelipad> helipads = helipadMap.getOrDefault(index, emptyList());
    if (helipads.isEmpty()) {
      return Map.of();
    }

    Map<String, HelipadPage> hm = new HashMap<>();
    //not great but needed because they can have landing systems and for some reason pads don't reference they systems
    helipads.forEach(helipad -> {
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
    return hm;
  }

  private static SupportingPage supportingPage(Map<Pair<String, String>, List<ArincNdbNavaid>> ndbNavaidMap,
                                               Map<Pair<String, String>, List<ArincVhfNavaid>> vhfNavaidMap,
                                               Map<Pair<String, String>, List<ArincWaypoint>> waypointMap,
                                               Map<Pair<String, String>, List<ArincProcedureLeg>> procedureLegMap,
                                               Pair<String, String> index) {

    List<ArincProcedureLeg> procedureLegs = procedureLegMap.getOrDefault(index, emptyList());
    List<ArincWaypoint> waypoints = waypointMap.getOrDefault(index, emptyList());
    List<ArincNdbNavaid> ndbNavaids = ndbNavaidMap.getOrDefault(index, emptyList());
    List<ArincVhfNavaid> vhfNavaids = vhfNavaidMap.getOrDefault(index, emptyList());

    Map<String, List<ArincProcedureLeg>> plm = procedureLegs.isEmpty()
        ? Map.of()
        : procedureLegs.stream().collect(Collectors.groupingBy(ArincProcedureLeg::sidStarIdentifier));

    // apply some nice-to-have sorting to the entries in the internal structure
    plm.keySet().forEach(key -> plm.computeIfPresent(key, (k, list) -> list.stream().sorted(procedureLegComparator).collect(Collectors.toList())));

    Map<String, ArincWaypoint> wm = waypoints.isEmpty()
        ? Map.of()
        : waypoints.stream().collect(Collectors.toMap(ArincWaypoint::waypointIdentifier, Function.identity()));

    Map<String, ArincNdbNavaid> nm = ndbNavaids.isEmpty()
        ? Map.of()
        : ndbNavaids.stream().collect(Collectors.toMap(ArincNdbNavaid::ndbIdentifier, Function.identity()));

    Map<String, ArincVhfNavaid> vm = vhfNavaids.isEmpty()
        ? Map.of()
        : vhfNavaids.stream().collect(Collectors.toMap(ArincVhfNavaid::vhfIdentifier, Function.identity()));

    return new SupportingPage(plm, wm, nm, vm);
  }

  private static <T, K> Map<K, List<T>> groupedBy(Collection<T> values, Function<T, K> indexer) {
    return values.isEmpty() ? Map.of() : values.stream().collect(Collectors.groupingBy(indexer));
  }
}
