package org.mitre.tdp.boogie.arinc.assemble;

import static java.util.stream.Collectors.groupingBy;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import org.mitre.tdp.boogie.arinc.model.ArincProcedureLeg;

/**
 * Groups ARINC procedure legs by the identity of their containing procedure without imposing an order across transitions.
 * Encounter order is retained within each procedure.
 */
final class ArincProcedureGrouper implements Function<Collection<ArincProcedureLeg>, Collection<List<ArincProcedureLeg>>> {

  static final ArincProcedureGrouper INSTANCE = new ArincProcedureGrouper();

  private ArincProcedureGrouper() {
  }

  @Override
  public Collection<List<ArincProcedureLeg>> apply(Collection<ArincProcedureLeg> procedureLegs) {
    return procedureLegs.stream()
        .collect(groupingBy(ArincProcedureGrouper::groupKey))
        .values();
  }

  private static ProcedureGroupKey groupKey(ArincProcedureLeg procedureLeg) {
    return new ProcedureGroupKey(
        procedureLeg.airportIdentifier(),
        procedureLeg.airportIcaoRegion(),
        procedureLeg.sidStarIdentifier(),
        procedureLeg.subSectionCode().orElseThrow(IllegalStateException::new)
    );
  }

  private record ProcedureGroupKey(
      String airportIdentifier,
      String airportIcaoRegion,
      String sidStarIdentifier,
      String subSectionCode
  ) {
  }
}
