package org.mitre.boogie.xml.database;

import java.util.List;

import org.mitre.tdp.boogie.Fix;
import org.mitre.boogie.xml.assemble.FixAssembler;
import org.mitre.boogie.xml.assemble.FixAssemblyStrategy;
import org.mitre.boogie.xml.model.ArincAirport;
import org.mitre.boogie.xml.model.ArincFixRecord;
import org.mitre.boogie.xml.model.ArincHeliport;
import org.mitre.boogie.xml.model.ArincRecords;
import org.mitre.boogie.xml.model.fields.ArincPortInfo;

/**
 * Factory for building a {@link FixDatabase} from parsed {@link ArincRecords}.
 *
 * <p>This factory walks all top-level records (waypoints, NDB navaids, VHF navaids) and all airport/heliport-nested
 * records (the port reference point, runways, gates, terminal waypoints, terminal NDBs, helipads,
 * localizer/glideslopes, markers, and GNSS landing systems) and indexes each by its XML reference ID.
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

    records.waypoints().forEach(wp -> indexRecord(builder, wp, assembler));
    records.ndbNavaids().forEach(ndb -> indexRecord(builder, ndb, assembler));
    records.vhfNavaids().forEach(vhf -> indexRecord(builder, vhf, assembler));
    records.airports().forEach(apt -> indexAirport(builder, apt, assembler));
    records.heliports().forEach(hp -> indexHeliport(builder, hp, assembler));

    return builder.build();
  }

  /**
   * Index a single fix-like record into the builder.
   */
  public static <F> void indexRecord(FixDatabase.Builder<F> builder, ArincFixRecord record, FixAssembler<F> assembler) {
    builder.index(record.pointInfo().referenceId(), assembler.assemble(record));
  }

  /**
   * Index an airport and all of its nested fix-like records (runways, gates, terminal waypoints, terminal NDBs,
   * helipads, localizer/glideslopes, markers, and GNSS landing systems) into the builder.
   */
  public static <F> void indexAirport(FixDatabase.Builder<F> builder, ArincAirport airport, FixAssembler<F> assembler) {
    indexPort(builder, airport.portInfo(), assembler);
    airport.runways().forEach(rwy -> indexRecord(builder, rwy, assembler));
    airport.airportGates().forEach(gate -> indexRecord(builder, gate, assembler));
  }

  /**
   * Index a heliport and all of its nested fix-like records into the builder.
   */
  public static <F> void indexHeliport(FixDatabase.Builder<F> builder, ArincHeliport heliport, FixAssembler<F> assembler) {
    indexPort(builder, heliport.portInfo(), assembler);
  }

  private static <F> void indexPort(FixDatabase.Builder<F> builder, ArincPortInfo portInfo, FixAssembler<F> assembler) {
    indexRecord(builder, portInfo, assembler);
    portInfo.terminalWaypoints().orElse(List.of()).forEach(wp -> indexRecord(builder, wp, assembler));
    portInfo.ndbNavaid().orElse(List.of()).forEach(ndb -> indexRecord(builder, ndb, assembler));
    portInfo.helipads().orElse(List.of()).forEach(hp -> indexRecord(builder, hp, assembler));
    portInfo.localizerGlideSlopes().orElse(List.of()).forEach(loc -> indexRecord(builder, loc, assembler));
    portInfo.markers().orElse(List.of()).forEach(mkr -> indexRecord(builder, mkr, assembler));
    portInfo.gnssLandingSystems().orElse(List.of()).forEach(gls -> indexRecord(builder, gls, assembler));
  }
}
