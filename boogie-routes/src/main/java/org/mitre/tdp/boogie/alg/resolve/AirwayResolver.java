package org.mitre.tdp.boogie.alg.resolve;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.alg.split.SectionSplit;
import org.mitre.tdp.boogie.alg.LookupService;

public final class AirwayResolver implements SingleSplitSectionResolver {

  private final LookupService<Airway> lookupService;

  public AirwayResolver(LookupService<Airway> lookupService) {
    this.lookupService = checkNotNull(lookupService);
  }

  @Override
  public List<ResolvedElement> resolve(SectionSplit sectionSplit) {
    return lookupService.apply(sectionSplit.value())
        .stream()
        .map(AirwayElement::new)
        .collect(Collectors.toList());
  }
}
