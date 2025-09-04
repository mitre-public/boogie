package org.mitre.tdp.boogie.alg.facade;

import java.util.function.Function;

import org.mitre.tdp.boogie.CategoryAndType;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.alg.LookupService;
import org.mitre.tdp.boogie.alg.RouteContext;
import org.mitre.tdp.boogie.fn.TriFunction;

final class ContextExtractor implements TriFunction<RouteDetails, LookupService<Procedure>, LookupService<Procedure>, RouteContext> {
  static final ContextExtractor INSTANCE = new ContextExtractor();
  private ContextExtractor() {}
  @Override
  public RouteContext apply(RouteDetails details, LookupService<Procedure> procedureService, LookupService<Procedure> proceduresAtAirport) {
    return RouteContext.standard()
        .proceduresByName(procedureService)
        .departureRunway(details.departureRunway().orElse(null))
        .arrivalRunway(details.arrivalRunway().orElse(null))
        .proceduresByAirport(proceduresAtAirport)
        .equipagePreference(details.equipagePreference())
        .defaultSid(details.defaultSid().orElse(null))
        .defaultStar(details.defaultStar().orElse(null))
        .categoryAndType(details.categoryAndType().orElse(CategoryAndType.NULL))
        .build();
  }
}
