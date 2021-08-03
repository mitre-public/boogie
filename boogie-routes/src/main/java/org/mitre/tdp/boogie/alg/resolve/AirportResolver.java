package org.mitre.tdp.boogie.alg.resolve;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.alg.LookupService;
import org.mitre.tdp.boogie.alg.split.SectionSplit;

public final class AirportResolver implements SingleSplitSectionResolver {

  private final LookupService<Airport> lookupService;

  public AirportResolver(LookupService<Airport> lookupService) {
    this.lookupService = checkNotNull(lookupService);
  }

  @Override
  public List<ResolvedElement> resolve(SectionSplit sectionSplit) {
    return lookupService
        .apply(sectionSplit.value())
        .stream()
        .map(airport -> new AirportElement(airport, sectionSplit.wildcards()))
        .collect(Collectors.toList());
  }
}
