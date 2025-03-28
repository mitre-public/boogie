package org.mitre.tdp.boogie.alg.resolve;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;
import static org.mitre.tdp.boogie.alg.resolve.ResolvedToken.directToAirport;
import static org.mitre.tdp.boogie.alg.resolve.ResolvedToken.standardAirport;
import static org.mitre.tdp.boogie.alg.split.RouteTokenVisitor.isDirectTo;

import java.util.List;

import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.alg.LookupService;
import org.mitre.tdp.boogie.alg.split.RouteToken;

final class AirportResolver implements SingleTokenResolver {

  private final LookupService<Airport> lookupService;

  AirportResolver(LookupService<Airport> lookupService) {
    this.lookupService = checkNotNull(lookupService);
  }

  @Override
  public List<ResolvedToken> resolve(RouteToken token) {
    return lookupService
        .apply(token.infrastructureName())
        .stream()
        .map(a -> isDirectTo(token) ? directToAirport(a) : standardAirport(a))
        .collect(toList());
  }
}
