package org.mitre.tdp.boogie.arinc;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.mitre.tdp.boogie.arinc.model.ArincAirport;
import org.mitre.tdp.boogie.arinc.model.ArincAirwayLeg;
import org.mitre.tdp.boogie.arinc.model.ArincControlledAirspaceLeg;
import org.mitre.tdp.boogie.arinc.model.ArincFirUirLeg;
import org.mitre.tdp.boogie.arinc.model.ArincGnssLandingSystem;
import org.mitre.tdp.boogie.arinc.model.ArincHeaderOne;
import org.mitre.tdp.boogie.arinc.model.ArincHelipad;
import org.mitre.tdp.boogie.arinc.model.ArincHeliport;
import org.mitre.tdp.boogie.arinc.model.ArincHoldingPattern;
import org.mitre.tdp.boogie.arinc.model.ArincLocalizerGlideSlope;
import org.mitre.tdp.boogie.arinc.model.ArincNdbNavaid;
import org.mitre.tdp.boogie.arinc.model.ArincProcedureLeg;
import org.mitre.tdp.boogie.arinc.model.ArincRunway;
import org.mitre.tdp.boogie.arinc.model.ArincVhfNavaid;
import org.mitre.tdp.boogie.arinc.model.ArincWaypoint;
import org.mitre.tdp.boogie.arinc.model.ConvertingArincRecordConsumer;

/**
 * The converted-record collections {@link OneshotRecordParser} assembles from, either passed straight through from one
 * consumer (identical to historical single-stream behavior) or merged across several prioritized consumers with
 * earliest-source-wins resolution per record identity.
 *
 * <p>Record identity is the ARINC spec key fields of each converted type — two records with equal identity describe the same
 * logical entity (possibly with different content) and are candidates for cross-source shadowing.
 */
final class RecordSets {

  private final Collection<ArincAirport> airports;
  private final Collection<ArincRunway> runways;
  private final Collection<ArincLocalizerGlideSlope> localizerGlideSlopes;
  private final Collection<ArincNdbNavaid> ndbNavaids;
  private final Collection<ArincVhfNavaid> vhfNavaids;
  private final Collection<ArincWaypoint> waypoints;
  private final Collection<ArincAirwayLeg> airwayLegs;
  private final Collection<ArincProcedureLeg> procedureLegs;
  private final Collection<ArincGnssLandingSystem> gnssLandingSystems;
  private final Collection<ArincHoldingPattern> holdingPatterns;
  private final Collection<ArincFirUirLeg> firUirLegs;
  private final Collection<ArincControlledAirspaceLeg> controlledAirspaceLegs;
  private final Collection<ArincHelipad> helipads;
  private final Collection<ArincHeliport> heliports;
  private final Optional<ArincHeaderOne> headerOne;

  private RecordSets(
      Collection<ArincAirport> airports,
      Collection<ArincRunway> runways,
      Collection<ArincLocalizerGlideSlope> localizerGlideSlopes,
      Collection<ArincNdbNavaid> ndbNavaids,
      Collection<ArincVhfNavaid> vhfNavaids,
      Collection<ArincWaypoint> waypoints,
      Collection<ArincAirwayLeg> airwayLegs,
      Collection<ArincProcedureLeg> procedureLegs,
      Collection<ArincGnssLandingSystem> gnssLandingSystems,
      Collection<ArincHoldingPattern> holdingPatterns,
      Collection<ArincFirUirLeg> firUirLegs,
      Collection<ArincControlledAirspaceLeg> controlledAirspaceLegs,
      Collection<ArincHelipad> helipads,
      Collection<ArincHeliport> heliports,
      Optional<ArincHeaderOne> headerOne) {
    this.airports = airports;
    this.runways = runways;
    this.localizerGlideSlopes = localizerGlideSlopes;
    this.ndbNavaids = ndbNavaids;
    this.vhfNavaids = vhfNavaids;
    this.waypoints = waypoints;
    this.airwayLegs = airwayLegs;
    this.procedureLegs = procedureLegs;
    this.gnssLandingSystems = gnssLandingSystems;
    this.holdingPatterns = holdingPatterns;
    this.firUirLegs = firUirLegs;
    this.controlledAirspaceLegs = controlledAirspaceLegs;
    this.helipads = helipads;
    this.heliports = heliports;
    this.headerOne = headerOne;
  }

  /** One source: the consumer's collections pass straight through, identical to historical single-stream behavior. */
  static RecordSets of(ConvertingArincRecordConsumer consumer) {
    return new RecordSets(
        consumer.arincAirports(),
        consumer.arincRunways(),
        consumer.arincLocalizerGlideSlopes(),
        consumer.arincNdbNavaids(),
        consumer.arincVhfNavaids(),
        consumer.arincWaypoints(),
        consumer.arincAirwayLegs(),
        consumer.arincProcedureLegs(),
        consumer.arincGnssLandingSystems(),
        consumer.arincHoldingPatterns(),
        consumer.arincFirUirLegs(),
        consumer.arincControlledAirspaceLegs(),
        consumer.arincHelipads(),
        consumer.arincHeliports(),
        consumer.arincHeaderOne()
    );
  }

  /** Several prioritized sources: earliest source wins per record identity; the header is the first one present. */
  static RecordSets merged(List<ConvertingArincRecordConsumer> ordered) {
    return new RecordSets(
        mergeByIdentity(ordered, ConvertingArincRecordConsumer::arincAirports, RecordSets::airportIdentity),
        mergeByIdentity(ordered, ConvertingArincRecordConsumer::arincRunways, RecordSets::runwayIdentity),
        mergeByIdentity(ordered, ConvertingArincRecordConsumer::arincLocalizerGlideSlopes, RecordSets::localizerIdentity),
        mergeByIdentity(ordered, ConvertingArincRecordConsumer::arincNdbNavaids, RecordSets::ndbIdentity),
        mergeByIdentity(ordered, ConvertingArincRecordConsumer::arincVhfNavaids, RecordSets::vhfIdentity),
        mergeByIdentity(ordered, ConvertingArincRecordConsumer::arincWaypoints, RecordSets::waypointIdentity),
        mergeByIdentity(ordered, ConvertingArincRecordConsumer::arincAirwayLegs, RecordSets::airwayLegIdentity),
        mergeByIdentity(ordered, ConvertingArincRecordConsumer::arincProcedureLegs, RecordSets::procedureLegIdentity),
        mergeByIdentity(ordered, ConvertingArincRecordConsumer::arincGnssLandingSystems, RecordSets::glsIdentity),
        mergeByIdentity(ordered, ConvertingArincRecordConsumer::arincHoldingPatterns, RecordSets::holdingPatternIdentity),
        mergeByIdentity(ordered, ConvertingArincRecordConsumer::arincFirUirLegs, RecordSets::firUirLegIdentity),
        mergeByIdentity(ordered, ConvertingArincRecordConsumer::arincControlledAirspaceLegs, RecordSets::controlledAirspaceLegIdentity),
        mergeByIdentity(ordered, ConvertingArincRecordConsumer::arincHelipads, RecordSets::helipadIdentity),
        mergeByIdentity(ordered, ConvertingArincRecordConsumer::arincHeliports, RecordSets::heliportIdentity),
        ordered.stream().map(ConvertingArincRecordConsumer::arincHeaderOne).flatMap(Optional::stream).findFirst()
    );
  }

  Collection<ArincAirport> airports() {
    return airports;
  }

  Collection<ArincRunway> runways() {
    return runways;
  }

  Collection<ArincLocalizerGlideSlope> localizerGlideSlopes() {
    return localizerGlideSlopes;
  }

  Collection<ArincNdbNavaid> ndbNavaids() {
    return ndbNavaids;
  }

  Collection<ArincVhfNavaid> vhfNavaids() {
    return vhfNavaids;
  }

  Collection<ArincWaypoint> waypoints() {
    return waypoints;
  }

  Collection<ArincAirwayLeg> airwayLegs() {
    return airwayLegs;
  }

  Collection<ArincProcedureLeg> procedureLegs() {
    return procedureLegs;
  }

  Collection<ArincGnssLandingSystem> gnssLandingSystems() {
    return gnssLandingSystems;
  }

  Collection<ArincHoldingPattern> holdingPatterns() {
    return holdingPatterns;
  }

  Collection<ArincFirUirLeg> firUirLegs() {
    return firUirLegs;
  }

  Collection<ArincControlledAirspaceLeg> controlledAirspaceLegs() {
    return controlledAirspaceLegs;
  }

  Collection<ArincHelipad> helipads() {
    return helipads;
  }

  Collection<ArincHeliport> heliports() {
    return heliports;
  }

  Optional<ArincHeaderOne> headerOne() {
    return headerOne;
  }

  /**
   * Union the sources' records of one type, keeping the first record seen for each identity — so earlier (higher-precedence)
   * sources shadow later ones, and a duplicated identity within one source keeps its first occurrence.
   */
  private static <T> Collection<T> mergeByIdentity(List<ConvertingArincRecordConsumer> ordered,
      Function<ConvertingArincRecordConsumer, Collection<T>> records, Function<T, Object> identity) {

    Map<Object, T> merged = new LinkedHashMap<>();
    for (ConvertingArincRecordConsumer source : ordered) {
      for (T record : records.apply(source)) {
        merged.putIfAbsent(identity.apply(record), record);
      }
    }
    return List.copyOf(merged.values());
  }

  private static Object airportIdentity(ArincAirport airport) {
    return Arrays.asList(airport.airportIdentifier(), airport.airportIcaoRegion());
  }

  private static Object runwayIdentity(ArincRunway runway) {
    return Arrays.asList(runway.airportIdentifier(), runway.airportIcaoRegion(), runway.runwayIdentifier());
  }

  private static Object localizerIdentity(ArincLocalizerGlideSlope localizer) {
    return Arrays.asList(localizer.airportIdentifier(), localizer.airportIcaoRegion(),
        localizer.localizerIdentifier(), localizer.runwayIdentifier());
  }

  private static Object ndbIdentity(ArincNdbNavaid ndb) {
    return Arrays.asList(ndb.sectionCode(), ndb.subSectionCode().orElse(null),
        ndb.airportIdentifier().orElse(null), ndb.ndbIdentifier(), ndb.ndbIcaoRegion());
  }

  private static Object vhfIdentity(ArincVhfNavaid vhf) {
    return Arrays.asList(vhf.airportIdentifier().orElse(null), vhf.vhfIdentifier(), vhf.vhfIcaoRegion());
  }

  private static Object waypointIdentity(ArincWaypoint waypoint) {
    return Arrays.asList(waypoint.sectionCode(), waypoint.subSectionCode().orElse(null),
        waypoint.airportIdentifier().orElse(null), waypoint.waypointIdentifier(), waypoint.waypointIcaoRegion());
  }

  private static Object airwayLegIdentity(ArincAirwayLeg leg) {
    return Arrays.asList(leg.routeIdentifier(), leg.sequenceNumber(), leg.fixIdentifier(), leg.fixIcaoRegion());
  }

  private static Object procedureLegIdentity(ArincProcedureLeg leg) {
    return Arrays.asList(leg.airportIdentifier(), leg.airportIcaoRegion(), leg.subSectionCode().orElse(null),
        leg.sidStarIdentifier(), leg.routeType(), leg.transitionIdentifier().orElse(null), leg.sequenceNumber());
  }

  private static Object glsIdentity(ArincGnssLandingSystem gls) {
    return Arrays.asList(gls.airportIdentifier(), gls.airportIcaoRegion(),
        gls.glsRefPathIdentifier(), gls.runwayIdentifier());
  }

  private static Object holdingPatternIdentity(ArincHoldingPattern holdingPattern) {
    return Arrays.asList(holdingPattern.regionCode().orElse(null), holdingPattern.icaoRegion().orElse(null),
        holdingPattern.fixIdentifier(), holdingPattern.fixIcaoRegion(),
        holdingPattern.duplicateIdentifier().orElse(null));
  }

  private static Object firUirLegIdentity(ArincFirUirLeg leg) {
    return Arrays.asList(leg.firUirIdentifier(), leg.firUirAddress().orElse(null),
        leg.firUirIndicator(), leg.sequenceNumber());
  }

  private static Object controlledAirspaceLegIdentity(ArincControlledAirspaceLeg leg) {
    return Arrays.asList(leg.icaoRegion(), leg.airspaceType(), leg.airspaceCenter(),
        leg.multipleCode().orElse(null), leg.sequenceNumber());
  }

  private static Object helipadIdentity(ArincHelipad helipad) {
    return Arrays.asList(helipad.airportHeliportIdentifier(), helipad.icaoCode(), helipad.helipadIdentifier());
  }

  private static Object heliportIdentity(ArincHeliport heliport) {
    return Arrays.asList(heliport.heliportIdentifier(), heliport.heliportIcaoRegion(),
        heliport.padIdentifier().orElse(null));
  }
}
