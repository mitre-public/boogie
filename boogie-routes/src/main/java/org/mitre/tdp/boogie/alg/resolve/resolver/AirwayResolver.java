package org.mitre.tdp.boogie.alg.resolve.resolver;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.alg.resolve.element.AirwayElement;
import org.mitre.tdp.boogie.alg.resolve.element.ResolvedElement;
import org.mitre.tdp.boogie.alg.split.SectionSplit;
import org.mitre.tdp.boogie.service.LookupService;

public final class AirwayResolver implements SingleSectionResolver {

  private final LookupService<Airway> lookupService;

  public AirwayResolver(LookupService<Airway> lookupService) {
    this.lookupService = checkNotNull(lookupService);
  }

  @Override
  public List<ResolvedElement<?>> resolve(SectionSplit sectionSplit) {
    return lookupService.allMatchingIdentifier(sectionSplit.value())
        .stream()
        .map(AirwayElement::new)
        .collect(Collectors.toList());
  }
}
