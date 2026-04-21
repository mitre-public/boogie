package org.mitre.boogie.xml;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.mitre.boogie.xml.assemble.AirportAssembler;
import org.mitre.boogie.xml.assemble.AirportAssemblyStrategy;
import org.mitre.boogie.xml.assemble.AirwayAssembler;
import org.mitre.boogie.xml.assemble.FixAssembler;
import org.mitre.boogie.xml.assemble.HeliportAssembler;
import org.mitre.boogie.xml.assemble.HeliportAssemblyStrategy;
import org.mitre.boogie.xml.assemble.ProcedureAssembler;
import org.mitre.boogie.xml.database.XmlFixDatabase;
import org.mitre.boogie.xml.database.FixDatabaseFactory;
import org.mitre.boogie.xml.database.PortPage;
import org.mitre.boogie.xml.database.XmlTerminalAreaDatabase;
import org.mitre.boogie.xml.model.ArincAirport;
import org.mitre.boogie.xml.model.fields.ArincPointInfo;
import org.mitre.boogie.xml.model.ArincAirway;
import org.mitre.boogie.xml.model.ArincHeliport;
import org.mitre.boogie.xml.model.ArincHoldingPattern;
import org.mitre.boogie.xml.model.ArincNdbNavaid;
import org.mitre.boogie.xml.model.ArincRecords;
import org.mitre.boogie.xml.model.ArincVhfNavaid;
import org.mitre.boogie.xml.model.ArincWaypoint;

/**
 * An {@link ArincRecords} implementation that assembles <i>all</i> record types on-the-fly during streaming
 * unmarshalling, eliminating buffering entirely. No intermediate model objects are retained after assembly.
 *
 * <p>This relies on the XML element ordering where waypoints, NDB navaids, VHF navaids, and airways appear
 * before airports and heliports in the stream. The {@link XmlFixDatabase} is created eagerly via
 * {@link XmlFixDatabase.Builder#build()} and shares its backing maps with the builder &mdash; so additions during
 * streaming are immediately visible to the assemblers that hold a reference to the built database.
 *
 * <p>During streaming:
 * <ul>
 *   <li><b>Waypoints, NDB navaids, VHF navaids</b> &mdash; immediately assembled into fixes and indexed in the
 *       {@link XmlFixDatabase.Builder}.</li>
 *   <li><b>Airways</b> &mdash; immediately assembled using the live {@link XmlFixDatabase} (enroute fixes already
 *       indexed).</li>
 *   <li><b>Airports</b> &mdash; terminal fixes indexed in the builder, a {@link PortPage} built and registered
 *       in the {@link XmlTerminalAreaDatabase.Builder}, the client airport assembled, and all procedures assembled
 *       inline using the live {@link XmlFixDatabase}.</li>
 *   <li><b>Heliports</b> &mdash; same as airports (minus procedures if none present).</li>
 *   <li><b>Holding patterns</b> &mdash; skipped (not used in assembly).</li>
 * </ul>
 *
 * @param <FIX>  the client fix type
 * @param <APT>  the client airport type
 * @param <RWY>  the client runway type
 * @param <AWY>  the client airway type
 * @param <PRC>  the client procedure type
 * @param <HLPD> the client helipad type
 * @param <HPT>  the client heliport type
 */
final class StreamAssemblyRecords<FIX, APT, RWY, AWY, PRC, HLPD, HPT> implements ArincRecords {

  private final FixAssembler<FIX> fixAssembler;
  private final AirportAssembler<APT> airportAssembler;
  private final AirportAssemblyStrategy<APT, RWY, HLPD> airportStrategy;
  private final AirwayAssembler<AWY> airwayAssembler;
  private final ProcedureAssembler<PRC> procedureAssembler;
  private final HeliportAssembler<HPT> heliportAssembler;
  private final HeliportAssemblyStrategy<HPT, HLPD> heliportStrategy;
  private final XmlFixDatabase.Builder<FIX> fixDatabaseBuilder;
  private final XmlTerminalAreaDatabase.Builder<FIX, RWY, HLPD> terminalAreaDatabaseBuilder;

  private final List<FIX> assembledFixes = new ArrayList<>();
  private final List<APT> assembledAirports = new ArrayList<>();
  private final List<AWY> assembledAirways = new ArrayList<>();
  private final List<PRC> assembledProcedures = new ArrayList<>();
  private final List<HPT> assembledHeliports = new ArrayList<>();

  StreamAssemblyRecords(
      FixAssembler<FIX> fixAssembler,
      AirportAssembler<APT> airportAssembler,
      AirportAssemblyStrategy<APT, RWY, HLPD> airportStrategy,
      AirwayAssembler<AWY> airwayAssembler,
      ProcedureAssembler<PRC> procedureAssembler,
      HeliportAssembler<HPT> heliportAssembler,
      HeliportAssemblyStrategy<HPT, HLPD> heliportStrategy,
      XmlFixDatabase.Builder<FIX> fixDatabaseBuilder) {
    this.fixAssembler = requireNonNull(fixAssembler);
    this.airportAssembler = requireNonNull(airportAssembler);
    this.airportStrategy = requireNonNull(airportStrategy);
    this.airwayAssembler = requireNonNull(airwayAssembler);
    this.procedureAssembler = requireNonNull(procedureAssembler);
    this.heliportAssembler = requireNonNull(heliportAssembler);
    this.heliportStrategy = requireNonNull(heliportStrategy);
    this.fixDatabaseBuilder = requireNonNull(fixDatabaseBuilder);
    this.terminalAreaDatabaseBuilder = XmlTerminalAreaDatabase.builder();
  }

  // ---------------------------------------------------------------------------
  // Add methods — called during streaming by XmlRecordHandlers
  // ---------------------------------------------------------------------------

  @Override
  public void addWaypoint(ArincWaypoint waypoint) {
    FIX fix = fixAssembler.assemble(waypoint);
    assembledFixes.add(fix);
    ArincPointInfo point = waypoint.pointInfo();
    fixDatabaseBuilder.index(point.referenceId(), fix);
    fixDatabaseBuilder.indexWaypoint(point.identifier(), point.icaoCode(), fix);
  }

  @Override
  public void addNdbNavaid(ArincNdbNavaid ndb) {
    FIX fix = fixAssembler.assemble(ndb);
    assembledFixes.add(fix);
    ArincPointInfo point = ndb.pointInfo();
    fixDatabaseBuilder.index(point.referenceId(), fix);
    fixDatabaseBuilder.indexNdbNavaid(point.identifier(), point.icaoCode(), fix);
  }

  @Override
  public void addVhfNavaid(ArincVhfNavaid vhf) {
    FIX fix = fixAssembler.assemble(vhf);
    assembledFixes.add(fix);
    ArincPointInfo point = vhf.pointInfo();
    fixDatabaseBuilder.index(point.referenceId(), fix);
    fixDatabaseBuilder.indexVhfNavaid(point.identifier(), point.icaoCode(), fix);
  }

  @Override
  public void addAirport(ArincAirport airport) {
    assembledAirports.add(airportAssembler.assembleOne(airport));
    PortPage<FIX, RWY, HLPD> page = FixDatabaseFactory.indexAirportPage(fixDatabaseBuilder, airport, fixAssembler, airportStrategy);
    terminalAreaDatabaseBuilder.withAirportPage(page);
    procedureAssembler.assemble(List.of(airport)).forEach(assembledProcedures::add);
  }

  @Override
  public void addHeliport(ArincHeliport heliport) {
    assembledHeliports.add(heliportAssembler.assembleOne(heliport));
    PortPage<FIX, RWY, HLPD> page = FixDatabaseFactory.indexHeliportPage(fixDatabaseBuilder, heliport, fixAssembler, heliportStrategy);
    terminalAreaDatabaseBuilder.withHeliportPage(page);
  }

  @Override
  public void addAirway(ArincAirway airway) {
    airwayAssembler.assemble(List.of(airway)).forEach(assembledAirways::add);
  }

  @Override
  public void addHoldingPattern(ArincHoldingPattern holdingPattern) {
    // no-op — holding patterns are not used in assembly
  }

  // ---------------------------------------------------------------------------
  // ArincRecords getters — return empty sets; no buffering is done
  // ---------------------------------------------------------------------------

  @Override
  public Set<ArincAirport> airports() {
    return Set.of();
  }

  @Override
  public Set<ArincAirway> arincAirways() {
    return Set.of();
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
  // Accessors for OneshotXmlParser to retrieve assembled results and databases
  // ---------------------------------------------------------------------------

  Collection<FIX> assembledFixes() {
    return assembledFixes;
  }

  Collection<APT> assembledAirports() {
    return assembledAirports;
  }

  Collection<AWY> assembledAirways() {
    return assembledAirways;
  }

  Collection<PRC> assembledProcedures() {
    return assembledProcedures;
  }

  Collection<HPT> assembledHeliports() {
    return assembledHeliports;
  }

  XmlTerminalAreaDatabase<FIX, RWY, HLPD> buildTerminalAreaDatabase() {
    return terminalAreaDatabaseBuilder.build();
  }
}
