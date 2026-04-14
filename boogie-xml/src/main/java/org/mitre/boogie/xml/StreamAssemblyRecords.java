package org.mitre.boogie.xml;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.mitre.boogie.xml.assemble.AirportAssembler;
import org.mitre.boogie.xml.assemble.FixAssembler;
import org.mitre.boogie.xml.assemble.HeliportAssembler;
import org.mitre.boogie.xml.database.FixDatabase;
import org.mitre.boogie.xml.database.FixDatabaseFactory;
import org.mitre.boogie.xml.model.ArincAirport;
import org.mitre.boogie.xml.model.ArincAirway;
import org.mitre.boogie.xml.model.ArincHeliport;
import org.mitre.boogie.xml.model.ArincHoldingPattern;
import org.mitre.boogie.xml.model.ArincNdbNavaid;
import org.mitre.boogie.xml.model.ArincRecords;
import org.mitre.boogie.xml.model.ArincVhfNavaid;
import org.mitre.boogie.xml.model.ArincWaypoint;

/**
 * An {@link ArincRecords} implementation that assembles independent record types on-the-fly during streaming
 * unmarshalling, reducing peak memory by not buffering intermediate model objects for types that don't require
 * cross-reference resolution.
 *
 * <p>During streaming:
 * <ul>
 *   <li><b>Waypoints, NDB navaids, VHF navaids</b> &mdash; immediately assembled into fixes and indexed in the
 *       {@link FixDatabase.Builder}. The intermediate model objects are not retained.</li>
 *   <li><b>Airports</b> &mdash; immediately assembled into client airport objects, nested fixes indexed in the
 *       builder, and the raw {@link ArincAirport} buffered for procedure assembly.</li>
 *   <li><b>Heliports</b> &mdash; immediately assembled into client heliport objects, nested fixes indexed. The
 *       intermediate model objects are not retained.</li>
 *   <li><b>Airways</b> &mdash; buffered for post-stream assembly (legs need the completed {@link FixDatabase}).</li>
 *   <li><b>Holding patterns</b> &mdash; skipped (not used in assembly).</li>
 * </ul>
 *
 * <p>After streaming, the caller retrieves the assembled collections and builds the {@link FixDatabase} from the
 * builder, then uses the standard {@code AirwayAssembler} and {@code ProcedureAssembler} against the buffered
 * airports and airways exposed through the {@link ArincRecords} getters.
 *
 * @param <FIX> the client fix type
 * @param <APT> the client airport type
 * @param <HPT> the client heliport type
 */
final class StreamAssemblyRecords<FIX, APT, HPT> implements ArincRecords {

  private final FixAssembler<FIX> fixAssembler;
  private final AirportAssembler<APT> airportAssembler;
  private final HeliportAssembler<HPT> heliportAssembler;
  private final FixDatabase.Builder<FIX> fixDatabaseBuilder;

  private final List<FIX> assembledFixes = new ArrayList<>();
  private final List<APT> assembledAirports = new ArrayList<>();
  private final List<HPT> assembledHeliports = new ArrayList<>();

  // Buffered for post-stream assembly — these record types need the completed FixDatabase.
  private final Set<ArincAirport> bufferedAirports = new HashSet<>();
  private final Set<ArincAirway> bufferedAirways = new HashSet<>();

  StreamAssemblyRecords(
      FixAssembler<FIX> fixAssembler,
      AirportAssembler<APT> airportAssembler,
      HeliportAssembler<HPT> heliportAssembler) {
    this.fixAssembler = requireNonNull(fixAssembler);
    this.airportAssembler = requireNonNull(airportAssembler);
    this.heliportAssembler = requireNonNull(heliportAssembler);
    this.fixDatabaseBuilder = FixDatabase.builder();
  }

  // ---------------------------------------------------------------------------
  // Add methods — called during streaming by XmlRecordHandlers
  // ---------------------------------------------------------------------------

  @Override
  public void addWaypoint(ArincWaypoint waypoint) {
    FIX fix = fixAssembler.assemble(waypoint);
    assembledFixes.add(fix);
    FixDatabaseFactory.indexRecord(fixDatabaseBuilder, waypoint, fixAssembler);
  }

  @Override
  public void addNdbNavaid(ArincNdbNavaid ndb) {
    FIX fix = fixAssembler.assemble(ndb);
    assembledFixes.add(fix);
    FixDatabaseFactory.indexRecord(fixDatabaseBuilder, ndb, fixAssembler);
  }

  @Override
  public void addVhfNavaid(ArincVhfNavaid vhf) {
    FIX fix = fixAssembler.assemble(vhf);
    assembledFixes.add(fix);
    FixDatabaseFactory.indexRecord(fixDatabaseBuilder, vhf, fixAssembler);
  }

  @Override
  public void addAirport(ArincAirport airport) {
    assembledAirports.add(airportAssembler.assembleOne(airport));
    FixDatabaseFactory.indexAirport(fixDatabaseBuilder, airport, fixAssembler);
    bufferedAirports.add(airport);
  }

  @Override
  public void addHeliport(ArincHeliport heliport) {
    assembledHeliports.add(heliportAssembler.assembleOne(heliport));
    FixDatabaseFactory.indexHeliport(fixDatabaseBuilder, heliport, fixAssembler);
  }

  @Override
  public void addAirway(ArincAirway airway) {
    bufferedAirways.add(airway);
  }

  @Override
  public void addHoldingPattern(ArincHoldingPattern holdingPattern) {
    // no-op — holding patterns are not used in assembly
  }

  // ---------------------------------------------------------------------------
  // Getters — called by post-stream assemblers (AirwayAssembler, ProcedureAssembler)
  // ---------------------------------------------------------------------------

  @Override
  public Set<ArincAirport> airports() {
    return bufferedAirports;
  }

  @Override
  public Set<ArincAirway> arincAirways() {
    return bufferedAirways;
  }

  @Override
  public Set<ArincWaypoint> waypoints() {
    return Set.of();
  }

  @Override
  public Set<ArincNdbNavaid> ndbNavaids() {
    return Set.of();
  }

  @Override
  public Set<ArincVhfNavaid> vhfNavaids() {
    return Set.of();
  }

  @Override
  public Set<ArincHoldingPattern> holdingPatterns() {
    return Set.of();
  }

  @Override
  public Set<ArincHeliport> heliports() {
    return Set.of();
  }

  // ---------------------------------------------------------------------------
  // Setters — not used in the streaming context
  // ---------------------------------------------------------------------------

  @Override
  public ArincRecords waypoints(Set<ArincWaypoint> waypoints) {
    return this;
  }

  @Override
  public ArincRecords airports(Set<ArincAirport> airports) {
    return this;
  }

  @Override
  public ArincRecords ndbNavaids(Set<ArincNdbNavaid> ndbNavaids) {
    return this;
  }

  @Override
  public ArincRecords vhfNavaids(Set<ArincVhfNavaid> vhfNavaids) {
    return this;
  }

  @Override
  public ArincRecords arincAirways(Set<ArincAirway> arincAirways) {
    return this;
  }

  @Override
  public ArincRecords holdingPatterns(Set<ArincHoldingPattern> holdingPatterns) {
    return this;
  }

  @Override
  public ArincRecords heliports(Set<ArincHeliport> heliports) {
    return this;
  }

  // ---------------------------------------------------------------------------
  // Accessors for OneshotXmlParser to retrieve assembled results
  // ---------------------------------------------------------------------------

  FixDatabase.Builder<FIX> fixDatabaseBuilder() {
    return fixDatabaseBuilder;
  }

  Collection<FIX> assembledFixes() {
    return assembledFixes;
  }

  Collection<APT> assembledAirports() {
    return assembledAirports;
  }

  Collection<HPT> assembledHeliports() {
    return assembledHeliports;
  }
}
