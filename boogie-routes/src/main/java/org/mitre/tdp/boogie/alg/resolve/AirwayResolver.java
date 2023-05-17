package org.mitre.tdp.boogie.alg.resolve;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.alg.LookupService;
import org.mitre.tdp.boogie.alg.split.RouteToken;

final class AirwayResolver implements SingleSplitSectionResolver {

  private final LookupService<Airway> lookupService;

  AirwayResolver(LookupService<Airway> lookupService) {
    this.lookupService = checkNotNull(lookupService);
  }

  @Override
  public List<ResolvedElement> resolve(RouteToken sectionSplit) {
    return lookupService.apply(sectionSplit.infrastructureName())
        .stream()
        .map(AirwayElement::new)
        .collect(Collectors.toList());
  }
}
