package org.mitre.tdp.boogie.alg.resolve.infer;

import static java.util.stream.Collectors.toList;

import java.util.Collection;

import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.alg.LookupService;

final class PreferProceduresAtAirport {

  private PreferProceduresAtAirport() {
  }

  static Collection<Procedure> lookup(LookupService<Procedure> proceduresByName, String procedureName, String airportName) {
    Collection<Procedure> procedures = proceduresByName.apply(procedureName);

    Collection<Procedure> proceduresAtAirport = procedures.stream()
        .filter(p -> p.airportIdentifier().equalsIgnoreCase(airportName))
        .collect(toList());

    return proceduresAtAirport.isEmpty() ? procedures : proceduresAtAirport;
  }
}
