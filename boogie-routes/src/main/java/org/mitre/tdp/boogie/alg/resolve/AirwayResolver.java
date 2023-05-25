package org.mitre.tdp.boogie.alg.resolve;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.alg.LookupService;
import org.mitre.tdp.boogie.alg.split.RouteToken;

final class AirwayResolver implements SingleTokenResolver {

  private final LookupService<Airway> lookupService;

  AirwayResolver(LookupService<Airway> lookupService) {
    this.lookupService = checkNotNull(lookupService);
  }

  @Override
  public List<ResolvedToken> resolve(RouteToken routeToken) {
    return lookupService.apply(routeToken.infrastructureName())
        .stream()
        .map(ResolvedToken::standardAirway)
        .collect(Collectors.toList());
  }
}
