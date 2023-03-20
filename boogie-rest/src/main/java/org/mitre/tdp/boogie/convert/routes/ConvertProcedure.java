package org.mitre.tdp.boogie.convert.routes;

import java.util.function.Function;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.contract.routes.ImmutableProcedure;
import org.mitre.tdp.boogie.contract.routes.Procedure;

public final class ConvertProcedure implements Function<org.mitre.tdp.boogie.Procedure, Procedure> {
  public static final ConvertProcedure INSTANCE = new ConvertProcedure();

  private static final ConvertTransition TRANS = ConvertTransition.INSTANCE;

  private ConvertProcedure() {

  }

  @Override
  public Procedure apply(org.mitre.tdp.boogie.Procedure procedure) {
    return ImmutableProcedure.builder()
        .procedureIdentifier(procedure.procedureIdentifier())
        .airportIdentifier(procedure.airportIdentifier())
        .airportRegion(procedure.airportRegion())
        .procedureType(procedure.procedureType())
        .requiredNavigationEquipage(procedure.requiredNavigationEquipage())
        .transitions(procedure.transitions().stream().map(TRANS).collect(Collectors.toList()))
        .build();
  }
}
