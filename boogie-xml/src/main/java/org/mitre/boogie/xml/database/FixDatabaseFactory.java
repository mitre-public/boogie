package org.mitre.boogie.xml.database;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.arinc.assemble.ReciprocalRunwayIdentifier;
import org.mitre.boogie.xml.assemble.AirportAssemblyStrategy;
import org.mitre.boogie.xml.assemble.FixAssembler;
import org.mitre.boogie.xml.assemble.FixAssemblyStrategy;
import org.mitre.boogie.xml.assemble.HeliportAssemblyStrategy;
import org.mitre.boogie.xml.model.ArincAirport;
import org.mitre.boogie.xml.model.ArincHeliport;
import org.mitre.boogie.xml.model.ArincLocalizerGlideSlope;
import org.mitre.boogie.xml.model.ArincRecords;
import org.mitre.boogie.xml.model.ArincRunway;
import org.mitre.boogie.xml.model.ArincWaypoint;
import org.mitre.boogie.xml.model.ArincNdbNavaid;
import org.mitre.boogie.xml.model.ArincVhfNavaid;
import org.mitre.boogie.xml.model.fields.ArincPointInfo;
import org.mitre.boogie.xml.model.fields.ArincPortInfo;

/**
 * Factory for building a {@link XmlFixDatabase} and {@link PortPage}s from parsed {@link ArincRecords}.
 *
 * <p>This factory walks all top-level records (waypoints, NDB navaids, VHF navaids) and all airport/heliport-nested
 * records (the port reference point, runways, gates, terminal waypoints, terminal NDBs, helipads,
 * localizer/glideslopes, markers, and GNSS landing systems) and indexes each by its XML reference ID as well as
 * by identifier and ICAO code.
 *
 * <p>The individual index methods are public so they can be reused during streaming unmarshalling with a
 * {@link XmlFixDatabase.Builder}.
 */
public final class FixDatabaseFactory {

  private static final ReciprocalRunwayIdentifier RECIPROCAL_ID = ReciprocalRunwayIdentifier.INSTANCE;

  private FixDatabaseFactory() {
  }

  /**
   * Create a fix database from the given records using the standard assembly strategy.
   */
  public static XmlFixDatabase<Fix> standard(ArincRecords records) {
    return create(records, FixAssemblyStrategy.standard());
  }

  /**
   * Create a fix database from the given records and assembly strategy.
   */
  public static <F> XmlFixDatabase<F> create(ArincRecords records, FixAssemblyStrategy<F> strategy) {
    FixAssembler<F> assembler = FixAssembler.withStrategy(strategy);
    XmlFixDatabase.Builder<F> builder = XmlFixDatabase.builder();

    records.waypoints().forEach(wp -> indexWaypoint(builder, wp, assembler));
    records.ndbNavaids().forEach(ndb -> indexNdbNavaid(builder, ndb, assembler));
    records.vhfNavaids().forEach(vhf -> indexVhfNavaid(builder, vhf, assembler));
    records.airports().forEach(apt -> indexAirport(builder, apt, assembler));
    records.heliports().forEach(hp -> indexHeliport(builder, hp, assembler));

    return builder.build();
  }

  /**
   * Index a waypoint by reference ID and by identifier + ICAO code.
   */
  public static <F> void indexWaypoint(XmlFixDatabase.Builder<F> builder, ArincWaypoint wp, FixAssembler<F> assembler) {
    ArincPointInfo point = wp.pointInfo();
    F fix = assembler.assemble(wp);
    builder.index(point.referenceId(), fix);
    builder.indexWaypoint(point.identifier(), point.icaoCode(), fix);
  }

  /**
   * Index an NDB navaid by reference ID and by identifier + ICAO code.
   */
  public static <F> void indexNdbNavaid(XmlFixDatabase.Builder<F> builder, ArincNdbNavaid ndb, FixAssembler<F> assembler) {
    ArincPointInfo point = ndb.pointInfo();
    F fix = assembler.assemble(ndb);
    builder.index(point.referenceId(), fix);
    builder.indexNdbNavaid(point.identifier(), point.icaoCode(), fix);
  }

  /**
   * Index a VHF navaid by reference ID and by identifier + ICAO code.
   */
  public static <F> void indexVhfNavaid(XmlFixDatabase.Builder<F> builder, ArincVhfNavaid vhf, FixAssembler<F> assembler) {
    ArincPointInfo point = vhf.pointInfo();
    F fix = assembler.assemble(vhf);
    builder.index(point.referenceId(), fix);
    builder.indexVhfNavaid(point.identifier(), point.icaoCode(), fix);
  }

  // ---------------------------------------------------------------------------
  // Fix-only indexing — used by create() where no PortPage is needed
  // ---------------------------------------------------------------------------

  /**
   * Index an airport and all of its nested records as fixes into the builder.
   */
  public static <F> void indexAirport(XmlFixDatabase.Builder<F> builder, ArincAirport airport, FixAssembler<F> assembler) {
    ArincPortInfo portInfo = airport.portInfo();
    ArincPointInfo point = portInfo.pointInfo();
    String portId = point.identifier();
    String icao = point.icaoCode();

    F airportFix = assembler.assemble(portInfo);
    builder.index(point.referenceId(), airportFix);
    builder.indexAirport(portId, icao, airportFix);

    indexPortChildrenFixes(builder, portInfo, portId, icao, assembler);

    airport.runways().forEach(rwy -> {
      F fix = assembler.assemble(rwy);
      builder.index(rwy.pointInfo().referenceId(), fix);
      builder.indexRunway(portId, icao, rwy.pointInfo().identifier(), fix);
    });
    airport.airportGates().forEach(gate -> {
      F fix = assembler.assemble(gate);
      builder.index(gate.pointInfo().referenceId(), fix);
      builder.indexGate(portId, icao, gate.pointInfo().identifier(), fix);
    });
  }

  /**
   * Index a heliport and all of its nested records as fixes into the builder.
   */
  public static <F> void indexHeliport(XmlFixDatabase.Builder<F> builder, ArincHeliport heliport, FixAssembler<F> assembler) {
    ArincPortInfo portInfo = heliport.portInfo();
    ArincPointInfo point = portInfo.pointInfo();
    String portId = point.identifier();
    String icao = point.icaoCode();

    F heliportFix = assembler.assemble(portInfo);
    builder.index(point.referenceId(), heliportFix);
    builder.indexHeliport(portId, icao, heliportFix);

    indexPortChildrenFixes(builder, portInfo, portId, icao, assembler);
  }

  // ---------------------------------------------------------------------------
  // PortPage-building — indexes fixes AND builds typed PortPage with R and H
  // ---------------------------------------------------------------------------

  /**
   * Index an airport's fixes into the builder and build a typed {@link PortPage} with assembled runways ({@code R})
   * and helipads ({@code H}) from the given strategy.
   */
  public static <F, R, H> PortPage<F, R, H> indexAirportPage(
      XmlFixDatabase.Builder<F> builder,
      ArincAirport airport,
      FixAssembler<F> fixAssembler,
      AirportAssemblyStrategy<?, R, H> airportStrategy) {

    ArincPortInfo portInfo = airport.portInfo();
    ArincPointInfo point = portInfo.pointInfo();

    F airportFix = fixAssembler.assemble(portInfo);
    builder.index(point.referenceId(), airportFix);
    builder.indexAirport(point.identifier(), point.icaoCode(), airportFix);

    PortPage.Builder<F, R, H> pageBuilder = PortPage.<F, R, H>builder()
        .referencePoint(airportFix)
        .identifier(point.identifier())
        .icaoCode(point.icaoCode());

    String portId = point.identifier();
    String icao = point.icaoCode();

    indexPortChildrenPage(builder, portInfo, portId, icao, fixAssembler, airportStrategy, pageBuilder);

    // Runways — pair origin/reciprocal and resolve ILS/GLS, same logic as AirportAssembler
    List<ArincRunway> arincRunways = airport.runways();

    Map<String, ArincRunway> runwayById = arincRunways.stream()
        .collect(Collectors.toMap(r -> r.pointInfo().identifier(), Function.identity(), (a, b) -> a));

    Map<String, ArincLocalizerGlideSlope> ilsByRunwayRef = portInfo.localizerGlideSlopes()
        .orElse(List.of()).stream()
        .filter(ils -> ils.runwayRef().isPresent())
        .collect(Collectors.toMap(ils -> ils.runwayRef().get(), Function.identity(), (a, b) -> a));

    for (ArincRunway origin : arincRunways) {
      String originId = origin.pointInfo().identifier();
      F rwyFix = fixAssembler.assemble(origin);
      builder.index(origin.pointInfo().referenceId(), rwyFix);
      builder.indexRunway(portId, icao, originId, rwyFix);

      ArincRunway reciprocal = RECIPROCAL_ID.apply(originId).map(runwayById::get).orElse(null);
      ArincLocalizerGlideSlope ilsGls1 = ilsByRunwayRef.get(originId);

      pageBuilder.addRunway(originId, airportStrategy.convertRunway(airport, origin, reciprocal, ilsGls1, null));
    }

    // Gates — fix-only (no domain type)
    airport.airportGates().forEach(gate -> {
      F fix = fixAssembler.assemble(gate);
      builder.index(gate.pointInfo().referenceId(), fix);
      builder.indexGate(portId, icao, gate.pointInfo().identifier(), fix);
      pageBuilder.addGate(gate.pointInfo().identifier(), fix);
    });

    return pageBuilder.build();
  }

  /**
   * Index a heliport's fixes into the builder and build a typed {@link PortPage} with assembled helipads ({@code H})
   * from the given strategy.
   */
  public static <F, R, H> PortPage<F, R, H> indexHeliportPage(
      XmlFixDatabase.Builder<F> builder,
      ArincHeliport heliport,
      FixAssembler<F> fixAssembler,
      HeliportAssemblyStrategy<?, H> heliportStrategy) {

    ArincPortInfo portInfo = heliport.portInfo();
    ArincPointInfo point = portInfo.pointInfo();

    F heliportFix = fixAssembler.assemble(portInfo);
    builder.index(point.referenceId(), heliportFix);
    builder.indexHeliport(point.identifier(), point.icaoCode(), heliportFix);

    PortPage.Builder<F, R, H> pageBuilder = PortPage.<F, R, H>builder()
        .referencePoint(heliportFix)
        .identifier(point.identifier())
        .icaoCode(point.icaoCode());

    String portId = point.identifier();
    String icao = point.icaoCode();

    indexPortChildrenPageHeliport(builder, portInfo, portId, icao, fixAssembler, heliportStrategy, pageBuilder);

    return pageBuilder.build();
  }

  // ---------------------------------------------------------------------------
  // Internal helpers
  // ---------------------------------------------------------------------------

  private static <F> void indexPortChildrenFixes(XmlFixDatabase.Builder<F> builder, ArincPortInfo portInfo,
      String portId, String icao, FixAssembler<F> assembler) {
    portInfo.terminalWaypoints().orElse(List.of()).forEach(wp -> {
      F fix = assembler.assemble(wp);
      builder.index(wp.pointInfo().referenceId(), fix);
      builder.indexTerminalWaypoint(portId, icao, wp.pointInfo().identifier(), fix);
    });
    portInfo.ndbNavaid().orElse(List.of()).forEach(ndb -> {
      F fix = assembler.assemble(ndb);
      builder.index(ndb.pointInfo().referenceId(), fix);
      builder.indexTerminalNdbNavaid(portId, icao, ndb.pointInfo().identifier(), fix);
    });
    portInfo.helipads().orElse(List.of()).forEach(hp -> {
      F fix = assembler.assemble(hp);
      builder.index(hp.pointInfo().referenceId(), fix);
      builder.indexTerminalHelipad(portId, icao, hp.pointInfo().identifier(), fix);
    });
    portInfo.localizerGlideSlopes().orElse(List.of()).forEach(loc -> {
      F fix = assembler.assemble(loc);
      builder.index(loc.pointInfo().referenceId(), fix);
      builder.indexLocalizerGlideSlope(portId, icao, loc.pointInfo().identifier(), fix);
    });
    portInfo.markers().orElse(List.of()).forEach(mkr -> {
      F fix = assembler.assemble(mkr);
      builder.index(mkr.pointInfo().referenceId(), fix);
      builder.indexMarker(portId, icao, mkr.pointInfo().identifier(), fix);
    });
    portInfo.gnssLandingSystems().orElse(List.of()).forEach(gls -> {
      F fix = assembler.assemble(gls);
      builder.index(gls.pointInfo().referenceId(), fix);
      builder.indexGnssLandingSystem(portId, icao, gls.pointInfo().identifier(), fix);
    });
  }

  private static <F, R, H> void indexPortChildrenPage(XmlFixDatabase.Builder<F> builder, ArincPortInfo portInfo,
      String portId, String icao, FixAssembler<F> fixAssembler, AirportAssemblyStrategy<?, R, H> airportStrategy,
      PortPage.Builder<F, R, H> pageBuilder) {
    portInfo.terminalWaypoints().orElse(List.of()).forEach(wp -> {
      F fix = fixAssembler.assemble(wp);
      builder.index(wp.pointInfo().referenceId(), fix);
      builder.indexTerminalWaypoint(portId, icao, wp.pointInfo().identifier(), fix);
      pageBuilder.addTerminalWaypoint(wp.pointInfo().identifier(), fix);
    });
    portInfo.ndbNavaid().orElse(List.of()).forEach(ndb -> {
      F fix = fixAssembler.assemble(ndb);
      builder.index(ndb.pointInfo().referenceId(), fix);
      builder.indexTerminalNdbNavaid(portId, icao, ndb.pointInfo().identifier(), fix);
      pageBuilder.addNdbNavaid(ndb.pointInfo().identifier(), fix);
    });
    portInfo.helipads().orElse(List.of()).forEach(hp -> {
      F fix = fixAssembler.assemble(hp);
      builder.index(hp.pointInfo().referenceId(), fix);
      builder.indexTerminalHelipad(portId, icao, hp.pointInfo().identifier(), fix);
      pageBuilder.addHelipad(hp.pointInfo().identifier(), airportStrategy.convertHelipad(hp));
    });
    portInfo.localizerGlideSlopes().orElse(List.of()).forEach(loc -> {
      F fix = fixAssembler.assemble(loc);
      builder.index(loc.pointInfo().referenceId(), fix);
      builder.indexLocalizerGlideSlope(portId, icao, loc.pointInfo().identifier(), fix);
      pageBuilder.addLocalizerGlideSlope(loc.pointInfo().identifier(), fix);
    });
    portInfo.markers().orElse(List.of()).forEach(mkr -> {
      F fix = fixAssembler.assemble(mkr);
      builder.index(mkr.pointInfo().referenceId(), fix);
      builder.indexMarker(portId, icao, mkr.pointInfo().identifier(), fix);
      pageBuilder.addMarker(mkr.pointInfo().identifier(), fix);
    });
    portInfo.gnssLandingSystems().orElse(List.of()).forEach(gls -> {
      F fix = fixAssembler.assemble(gls);
      builder.index(gls.pointInfo().referenceId(), fix);
      builder.indexGnssLandingSystem(portId, icao, gls.pointInfo().identifier(), fix);
      pageBuilder.addGnssLandingSystem(gls.pointInfo().identifier(), fix);
    });
  }

  private static <F, R, H> void indexPortChildrenPageHeliport(XmlFixDatabase.Builder<F> builder, ArincPortInfo portInfo,
      String portId, String icao, FixAssembler<F> fixAssembler, HeliportAssemblyStrategy<?, H> heliportStrategy,
      PortPage.Builder<F, R, H> pageBuilder) {
    portInfo.terminalWaypoints().orElse(List.of()).forEach(wp -> {
      F fix = fixAssembler.assemble(wp);
      builder.index(wp.pointInfo().referenceId(), fix);
      builder.indexTerminalWaypoint(portId, icao, wp.pointInfo().identifier(), fix);
      pageBuilder.addTerminalWaypoint(wp.pointInfo().identifier(), fix);
    });
    portInfo.ndbNavaid().orElse(List.of()).forEach(ndb -> {
      F fix = fixAssembler.assemble(ndb);
      builder.index(ndb.pointInfo().referenceId(), fix);
      builder.indexTerminalNdbNavaid(portId, icao, ndb.pointInfo().identifier(), fix);
      pageBuilder.addNdbNavaid(ndb.pointInfo().identifier(), fix);
    });
    portInfo.helipads().orElse(List.of()).forEach(hp -> {
      F fix = fixAssembler.assemble(hp);
      builder.index(hp.pointInfo().referenceId(), fix);
      builder.indexTerminalHelipad(portId, icao, hp.pointInfo().identifier(), fix);
      pageBuilder.addHelipad(hp.pointInfo().identifier(), heliportStrategy.convertHelipad(hp));
    });
    portInfo.localizerGlideSlopes().orElse(List.of()).forEach(loc -> {
      F fix = fixAssembler.assemble(loc);
      builder.index(loc.pointInfo().referenceId(), fix);
      builder.indexLocalizerGlideSlope(portId, icao, loc.pointInfo().identifier(), fix);
      pageBuilder.addLocalizerGlideSlope(loc.pointInfo().identifier(), fix);
    });
    portInfo.markers().orElse(List.of()).forEach(mkr -> {
      F fix = fixAssembler.assemble(mkr);
      builder.index(mkr.pointInfo().referenceId(), fix);
      builder.indexMarker(portId, icao, mkr.pointInfo().identifier(), fix);
      pageBuilder.addMarker(mkr.pointInfo().identifier(), fix);
    });
    portInfo.gnssLandingSystems().orElse(List.of()).forEach(gls -> {
      F fix = fixAssembler.assemble(gls);
      builder.index(gls.pointInfo().referenceId(), fix);
      builder.indexGnssLandingSystem(portId, icao, gls.pointInfo().identifier(), fix);
      pageBuilder.addGnssLandingSystem(gls.pointInfo().identifier(), fix);
    });
  }
}
