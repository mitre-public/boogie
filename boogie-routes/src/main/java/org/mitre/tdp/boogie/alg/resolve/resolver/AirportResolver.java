package org.mitre.tdp.boogie.alg.resolve.resolver;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.alg.resolve.element.AirportElement;
import org.mitre.tdp.boogie.alg.resolve.element.ResolvedElement;
import org.mitre.tdp.boogie.alg.split.SectionSplit;
import org.mitre.tdp.boogie.service.LookupService;

public final class AirportResolver implements SingleSectionResolver {

  private final LookupService<Airport> lookupService;

  public AirportResolver(LookupService<Airport> lookupService) {
    this.lookupService = checkNotNull(lookupService);
  }

  @Override
  public List<ResolvedElement<?>> resolve(SectionSplit sectionSplit) {
    return lookupService
        .allMatchingIdentifier(sectionSplit.value())
        .stream()
        .map(AirportElement::new)
        .collect(Collectors.toList());
  }
}
