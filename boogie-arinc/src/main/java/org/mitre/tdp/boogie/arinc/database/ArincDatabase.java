package org.mitre.tdp.boogie.arinc.database;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

/**
 * Class representing an in-memory version of a navigation database based around ARINC-424 formatted navigation data.
 * <br>
 * This class represents a set of multi-indexed database
 */
public final class ArincDatabase {

  private final ImmutableMultimap<ArincKey, ArincRecord> recordLookup;

  ArincDatabase(Multimap<ArincKey, ArincRecord> recordLookup) {
    this.recordLookup = ImmutableMultimap.copyOf(recordLookup);
  }

  /**
   * Returns the {@link ArincRecord} for a terminal-area navaid matching the provided identifier and ICAO region.
   */
  public Optional<ArincRecord> terminalNdbNavaid(String navaidIdentifier, String icaoRegion) {
    return lookupRecord(requireNonNull(navaidIdentifier), requireNonNull(icaoRegion), SectionCode.D, "B");
  }

  /**
   * Returns the {@link ArincRecord} for an enroute navaid matching the provided identifier and ICAO region.
   */
  public Optional<ArincRecord> enrouteNdbNavaid(String navaidIdentifier, String icaoRegion) {
    return lookupRecord(requireNonNull(navaidIdentifier), requireNonNull(icaoRegion), SectionCode.P, "N");
  }

  /**
   * Returns the {@link ArincRecord} for a VHF navaid matching the provided identifier and ICAO region.
   */
  public Optional<ArincRecord> vhfNavaid(String navaidIdentifier, String icaoRegion) {
    return lookupRecord(navaidIdentifier, icaoRegion, SectionCode.D, null);
  }

  /**
   * Returns the {@link ArincRecord} for a terminal area waypoint matching the provided identifier and ICAO region.
   */
  public Optional<ArincRecord> terminalWaypoint(String waypointIdentifier, String icaoRegion) {
    return lookupRecord(waypointIdentifier, icaoRegion, SectionCode.P, "C");
  }

  /**
   * Returns the {@link ArincRecord} for an enroute waypoint matching the provided identifier and ICAO region.
   */
  public Optional<ArincRecord> enrouteWaypoint(String waypointIdentifier, String icaoRegion) {
    return lookupRecord(waypointIdentifier, icaoRegion, SectionCode.E, "A");
  }

  /**
   * Returns the {@link ArincRecord} representing the provided airport within the supplied ICAO region.
   */
  public Optional<ArincRecord> airport(String airportIdentifier, String icaoRegion) {
    return lookupRecord(airportIdentifier, icaoRegion, SectionCode.P, "A");
  }

  /**
   * Returns the collection of all Airway (leg) records (Section/SubSection = ER) matching the provided airway identifier and
   * {@link CustomerAreaCode} within the database.
   */
  public Collection<ArincRecord> airwayLegs(String airway, CustomerAreaCode customerAreaCode) {
    return lookupRecords(airway, customerAreaCode.name(), SectionCode.E, "R");
  }

  /**
   * Returns the collection of all Approach (leg) records (Section/SubSection = PF) matching the provided procedure name and
   * airport identifier.
   */
  public Collection<ArincRecord> approachLegs(String procedure, String airport) {
    return lookupRecords(procedure, airport, SectionCode.P, "F");
  }

  /**
   * Returns the collection of all SID (leg) records (Section/SubSection = PD) matching the provided procedure name and
   * airport identifier.
   */
  public Collection<ArincRecord> sidLegs(String procedure, String airport) {
    return lookupRecords(procedure, airport, SectionCode.P, "D");
  }

  /**
   * Returns the collection of all STAR (leg) records (Section/SubSection = PE) matching the provided procedure name and
   * airport identifier.
   */
  public Collection<ArincRecord> starLegs(String procedure, String airport) {
    return lookupRecords(procedure, airport, SectionCode.P, "E");
  }

  /**
   * Returns the unique record matching the provided:
   * <br>
   * 1. Identifier - e.g. waypoint ID, airport ID, etc.
   * 2. ICAO Region - e.g. K2, CZ, K6, etc.
   * 3. Section -
   * 4. SubSection -
   * <br>
   * If multiple records are matched (e.g. multiple procedure legs - or there is a bad internal mapping in the database for a
   * particular identifier/region/section/subsection combination) this returns {@link Optional#empty()}.
   */
  public Optional<ArincRecord> lookupRecord(String identifier, String icaoRegion, SectionCode section, String subSection) {
    return highlander(lookupRecords(identifier, icaoRegion, section, subSection));
  }

  /**
   * Returns all record within the database matching the provided input parameters.
   */
  public Collection<ArincRecord> lookupRecords(String identifier, String icaoRegion, SectionCode section, String subSection) {
    return recordLookup.get(new ArincKey(identifier, icaoRegion, section, subSection));
  }

  /**
   * Returns a stream of all of the {@link ArincRecord}s with the {@link ArincDatabase}. This method is package-private to allow
   * related databases to re-index the records of a given ArincDatabase instantiation for alternate query types.
   * <br>
   * e.g. {@link TerminalDatabase}
   */
  Stream<ArincRecord> streamAllRecords() {
    return recordLookup.entries().stream().map(Map.Entry::getValue);
  }

  private <T> Optional<T> highlander(Collection<T> col) {
    return col.size() == 1 ? Optional.of(col.iterator().next()) : Optional.empty();
  }
}
