package org.mitre.tdp.boogie.alg.resolve;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.alg.LookupService;
import org.mitre.tdp.boogie.alg.split.RouteToken;
import org.mitre.tdp.boogie.alg.split.RouteTokenVisitor;

final class AirportResolver implements SingleTokenResolver {

  private final LookupService<Airport> lookupService;

  AirportResolver(LookupService<Airport> lookupService) {
    this.lookupService = checkNotNull(lookupService);
  }

  @Override
  public List<ResolvedElement> resolve(RouteToken sectionSplit) {
    return lookupService
        .apply(sectionSplit.infrastructureName())
        .stream()
        .map(airport -> new AirportElement(airport, RouteTokenVisitor.wildcards(sectionSplit)))
        .collect(Collectors.toList());
  }
}
