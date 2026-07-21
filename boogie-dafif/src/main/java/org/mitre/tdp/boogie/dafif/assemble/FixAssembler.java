package org.mitre.tdp.boogie.dafif.assemble;

import java.util.Collection;

import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.dafif.database.DafifFixDatabase;
import org.mitre.tdp.boogie.dafif.database.DafifTerminalAreaDatabase;
import org.mitre.tdp.boogie.dafif.model.DafifAddRunway;
import org.mitre.tdp.boogie.dafif.model.DafifAirport;
import org.mitre.tdp.boogie.dafif.model.DafifIls;
import org.mitre.tdp.boogie.dafif.model.DafifModel;
import org.mitre.tdp.boogie.dafif.model.DafifNavaid;
import org.mitre.tdp.boogie.dafif.model.DafifRunway;
import org.mitre.tdp.boogie.dafif.model.DafifWaypoint;

/**
 * This class assembles DAFIF fix suppliers into Fix objects. Note this does require parsing as dafif has many supplier/consumer relationships.
 * @param <F> the class of the output fix.
 */
public interface FixAssembler<F> {

  static FixAssembler<Fix> standard(DafifTerminalAreaDatabase tad, DafifFixDatabase fdb) {
    return withStrategy(tad, fdb, FixAssemblyStrategy.standard());
  }

  static <F> FixAssembler<F> withStrategy(DafifTerminalAreaDatabase tad, DafifFixDatabase fdb, FixAssemblyStrategy<F> strategy) {
    return new Standard<>(tad, fdb, strategy);
  }

  Collection<F> assemble(DafifModel model);

  final class Standard<F> implements FixAssembler<F> {

    private final DafifTerminalAreaDatabase tad;
    private final DafifFixDatabase fdb;
    private final FixAssemblyStrategy<F> strategy;

    private Standard(DafifTerminalAreaDatabase tad, DafifFixDatabase fdb, FixAssemblyStrategy<F> strategy) {
      this.tad = tad;
      this.fdb = fdb;
      this.strategy = strategy;
    }

    @Override
    public Collection<F> assemble(DafifModel model) {
      return switch (model.getFileType()) {
        case ILS -> strategy.convertIls((DafifIls) model);
        case NAVAID -> strategy.convertNavaid((DafifNavaid) model);
        case RUNWAY -> {
          DafifRunway runway = (DafifRunway) model;
          DafifAddRunway addRunway = tad.addRunwayFor(runway).orElse(null);
          DafifAirport airport = tad.airport(runway.airportIdentification()).orElse(null);
          yield strategy.convertRunway(runway, addRunway, airport);
        }
        case WAYPOINT -> {
          DafifWaypoint waypoint = (DafifWaypoint) model;
          yield strategy.convertWaypoint(waypoint, fdb.navaidFor(waypoint).orElse(null));
        }
        case AIRPORT -> strategy.convertAirport((DafifAirport) model);
        default -> throw new IllegalArgumentException("Unsupported DAFIF file type for fixes: " + model.getFileType());
      };
    }
  }
}
