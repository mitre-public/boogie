package org.mitre.boogie.xml.database;

import java.util.List;

import org.mitre.tdp.boogie.Fix;
import org.mitre.boogie.xml.assemble.FixAssembler;
import org.mitre.boogie.xml.assemble.FixAssemblyStrategy;
import org.mitre.boogie.xml.model.ArincAirport;
import org.mitre.boogie.xml.model.ArincHeliport;
import org.mitre.boogie.xml.model.ArincRecords;
import org.mitre.boogie.xml.model.ArincWaypoint;
import org.mitre.boogie.xml.model.ArincNdbNavaid;
import org.mitre.boogie.xml.model.ArincVhfNavaid;
import org.mitre.boogie.xml.model.fields.ArincPointInfo;
import org.mitre.boogie.xml.model.fields.ArincPortInfo;

/**
 * Factory for building a {@link FixDatabase} and {@link PortPage}s from parsed {@link ArincRecords}.
 *
 * <p>This factory walks all top-level records (waypoints, NDB navaids, VHF navaids) and all airport/heliport-nested
 * records (the port reference point, runways, gates, terminal waypoints, terminal NDBs, helipads,
 * localizer/glideslopes, markers, and GNSS landing systems) and indexes each by its XML reference ID as well as
 * by identifier and ICAO code.
 *
 * <p>The individual index methods are public so they can be reused during streaming unmarshalling with a
 * {@link FixDatabase.Builder}.
 */
public final class FixDatabaseFactory {

  private FixDatabaseFactory() {
  }

  /**
   * Create a fix database from the given records using the standard assembly strategy.
   */
  public static FixDatabase<Fix> standard(ArincRecords records) {
    return create(records, FixAssemblyStrategy.standard());
  }

  /**
   * Create a fix database from the given records and assembly strategy.
   */
  public static <F> FixDatabase<F> create(ArincRecords records, FixAssemblyStrategy<F> strategy) {
    FixAssembler<F> assembler = FixAssembler.withStrategy(strategy);
    FixDatabase.Builder<F> builder = FixDatabase.builder();

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
  public static <F> void indexWaypoint(FixDatabase.Builder<F> builder, ArincWaypoint wp, FixAssembler<F> assembler) {
    ArincPointInfo point = wp.pointInfo();
    F fix = assembler.assemble(wp);
    builder.index(point.referenceId(), fix);
    builder.indexWaypoint(point.identifier(), point.icaoCode(), fix);
  }

  /**
   * Index an NDB navaid by reference ID and by identifier + ICAO code.
   */
  public static <F> void indexNdbNavaid(FixDatabase.Builder<F> builder, ArincNdbNavaid ndb, FixAssembler<F> assembler) {
    ArincPointInfo point = ndb.pointInfo();
    F fix = assembler.assemble(ndb);
    builder.index(point.referenceId(), fix);
    builder.indexNdbNavaid(point.identifier(), point.icaoCode(), fix);
  }

  /**
   * Index a VHF navaid by reference ID and by identifier + ICAO code.
   */
  public static <F> void indexVhfNavaid(FixDatabase.Builder<F> builder, ArincVhfNavaid vhf, FixAssembler<F> assembler) {
    ArincPointInfo point = vhf.pointInfo();
    F fix = assembler.assemble(vhf);
    builder.index(point.referenceId(), fix);
    builder.indexVhfNavaid(point.identifier(), point.icaoCode(), fix);
  }

  /**
   * Index an airport and all of its nested fix-like records into the builder. Returns a {@link PortPage} containing
   * the assembled terminal fixes scoped to this airport.
   */
  public static <F> PortPage<F> indexAirport(FixDatabase.Builder<F> builder, ArincAirport airport, FixAssembler<F> assembler) {
    ArincPortInfo portInfo = airport.portInfo();
    ArincPointInfo point = portInfo.pointInfo();

    F airportFix = assembler.assemble(portInfo);
    builder.index(point.referenceId(), airportFix);
    builder.indexAirport(point.identifier(), point.icaoCode(), airportFix);

    PortPage.Builder<F> pageBuilder = PortPage.<F>builder()
        .referencePoint(airportFix)
        .identifier(point.identifier())
        .icaoCode(point.icaoCode());

    indexPortChildren(builder, portInfo, assembler, pageBuilder);

    airport.runways().forEach(rwy -> {
      F fix = assembler.assemble(rwy);
      builder.index(rwy.pointInfo().referenceId(), fix);
      pageBuilder.addRunway(rwy.pointInfo().identifier(), fix);
    });

    airport.airportGates().forEach(gate -> {
      F fix = assembler.assemble(gate);
      builder.index(gate.pointInfo().referenceId(), fix);
      pageBuilder.addGate(gate.pointInfo().identifier(), fix);
    });

    return pageBuilder.build();
  }

  /**
   * Index a heliport and all of its nested fix-like records into the builder. Returns a {@link PortPage} containing
   * the assembled terminal fixes scoped to this heliport.
   */
  public static <F> PortPage<F> indexHeliport(FixDatabase.Builder<F> builder, ArincHeliport heliport, FixAssembler<F> assembler) {
    ArincPortInfo portInfo = heliport.portInfo();
    ArincPointInfo point = portInfo.pointInfo();

    F heliportFix = assembler.assemble(portInfo);
    builder.index(point.referenceId(), heliportFix);
    builder.indexHeliport(point.identifier(), point.icaoCode(), heliportFix);

    PortPage.Builder<F> pageBuilder = PortPage.<F>builder()
        .referencePoint(heliportFix)
        .identifier(point.identifier())
        .icaoCode(point.icaoCode());

    indexPortChildren(builder, portInfo, assembler, pageBuilder);

    return pageBuilder.build();
  }

  private static <F> void indexPortChildren(FixDatabase.Builder<F> builder, ArincPortInfo portInfo,
      FixAssembler<F> assembler, PortPage.Builder<F> pageBuilder) {
    portInfo.terminalWaypoints().orElse(List.of()).forEach(wp -> {
      F fix = assembler.assemble(wp);
      builder.index(wp.pointInfo().referenceId(), fix);
      pageBuilder.addTerminalWaypoint(wp.pointInfo().identifier(), fix);
    });
    portInfo.ndbNavaid().orElse(List.of()).forEach(ndb -> {
      F fix = assembler.assemble(ndb);
      builder.index(ndb.pointInfo().referenceId(), fix);
      pageBuilder.addNdbNavaid(ndb.pointInfo().identifier(), fix);
    });
    portInfo.helipads().orElse(List.of()).forEach(hp -> {
      F fix = assembler.assemble(hp);
      builder.index(hp.pointInfo().referenceId(), fix);
      pageBuilder.addHelipad(hp.pointInfo().identifier(), fix);
    });
    portInfo.localizerGlideSlopes().orElse(List.of()).forEach(loc -> {
      F fix = assembler.assemble(loc);
      builder.index(loc.pointInfo().referenceId(), fix);
      pageBuilder.addLocalizerGlideSlope(loc.pointInfo().identifier(), fix);
    });
    portInfo.markers().orElse(List.of()).forEach(mkr -> {
      F fix = assembler.assemble(mkr);
      builder.index(mkr.pointInfo().referenceId(), fix);
      pageBuilder.addMarker(mkr.pointInfo().identifier(), fix);
    });
    portInfo.gnssLandingSystems().orElse(List.of()).forEach(gls -> {
      F fix = assembler.assemble(gls);
      builder.index(gls.pointInfo().referenceId(), fix);
      pageBuilder.addGnssLandingSystem(gls.pointInfo().identifier(), fix);
    });
  }
}
