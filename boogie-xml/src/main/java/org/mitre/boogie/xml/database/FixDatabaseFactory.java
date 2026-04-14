package org.mitre.boogie.xml.database;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    Map<String, F> index = new HashMap<>();

    records.waypoints().forEach(wp -> index(index, wp, assembler));
    records.ndbNavaids().forEach(ndb -> index(index, ndb, assembler));
    records.vhfNavaids().forEach(vhf -> index(index, vhf, assembler));
    records.airports().forEach(apt -> indexAirport(index, apt, assembler));
    records.heliports().forEach(hp -> indexHeliport(index, hp, assembler));

    return new FixDatabase<>(index);
  }

  private static <F> void index(Map<String, F> index, ArincFixRecord record, FixAssembler<F> assembler) {
    index.put(record.pointInfo().referenceId(), assembler.assemble(record));
  }

  private static <F> void indexAirport(Map<String, F> index, ArincAirport airport, FixAssembler<F> assembler) {
    indexPort(index, airport.portInfo(), assembler);
    airport.runways().forEach(rwy -> index(index, rwy, assembler));
    airport.airportGates().forEach(gate -> index(index, gate, assembler));
  }

  private static <F> void indexHeliport(Map<String, F> index, ArincHeliport heliport, FixAssembler<F> assembler) {
    indexPort(index, heliport.portInfo(), assembler);
  }

  private static <F> void indexPort(Map<String, F> index, ArincPortInfo portInfo, FixAssembler<F> assembler) {
    index(index, portInfo, assembler);
    portInfo.terminalWaypoints().orElse(List.of()).forEach(wp -> index(index, wp, assembler));
    portInfo.ndbNavaid().orElse(List.of()).forEach(ndb -> index(index, ndb, assembler));
    portInfo.helipads().orElse(List.of()).forEach(hp -> index(index, hp, assembler));
    portInfo.localizerGlideSlopes().orElse(List.of()).forEach(loc -> index(index, loc, assembler));
    portInfo.markers().orElse(List.of()).forEach(mkr -> index(index, mkr, assembler));
    portInfo.gnssLandingSystems().orElse(List.of()).forEach(gls -> index(index, gls, assembler));
  }
}
