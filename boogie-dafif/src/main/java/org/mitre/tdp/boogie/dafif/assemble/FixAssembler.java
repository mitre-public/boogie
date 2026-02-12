package org.mitre.tdp.boogie.dafif.assemble;

import java.util.Collection;

import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.dafif.database.DafifFixDatabase;
import org.mitre.tdp.boogie.dafif.database.DafifTerminalAreaDatabase;
import org.mitre.tdp.boogie.dafif.model.DafifIls;
import org.mitre.tdp.boogie.dafif.model.DafifModel;
import org.mitre.tdp.boogie.dafif.model.DafifNavaid;
import org.mitre.tdp.boogie.dafif.model.DafifRunway;
import org.mitre.tdp.boogie.dafif.model.DafifWaypoint;

public interface FixAssembler<F> {

  static FixAssembler<Fix> standard(DafifTerminalAreaDatabase terminalAreaDatabase, DafifFixDatabase fixDatabase) {
    return new Standard<>(FixAssemblyStrategy.standard(terminalAreaDatabase, fixDatabase));
  }

  Collection<F> assemble(DafifModel model);

  final class Standard<F> implements FixAssembler<F> {

    private final FixAssemblyStrategy<F> strategy;

    private Standard(FixAssemblyStrategy<F> strategy) {
      this.strategy = strategy;
    }

    @Override
    public Collection<F> assemble(DafifModel model) {
      return switch (model.getFileType()) {
        case ILS -> strategy.convertIls((DafifIls) model);
        case NAVAID ->  strategy.convertNavaid((DafifNavaid) model);
        case RUNWAY ->  strategy.convertRunway((DafifRunway) model);
        case WAYPOINT ->  strategy.convertWaypoint((DafifWaypoint) model);
        default -> throw new IllegalArgumentException("Unsupported DAFIF file type for fixes: " + model.getFileType());
      };
    }
  }
}
