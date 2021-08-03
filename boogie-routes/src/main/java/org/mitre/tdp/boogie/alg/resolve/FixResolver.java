package org.mitre.tdp.boogie.alg.resolve;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.mitre.tdp.boogie.alg.resolve.SectionHeuristics.tailored;

import java.util.List;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.alg.LookupService;
import org.mitre.tdp.boogie.alg.split.SectionSplit;

public final class FixResolver implements SingleSplitSectionResolver {

  private final LookupService<Fix> lookupService;

  public FixResolver(LookupService<Fix> lookupService) {
    this.lookupService = checkNotNull(lookupService);
  }

  @Override
  public List<ResolvedElement> resolve(SectionSplit sectionSplit) {
    String section = sectionSplit.value();

    // check to see if the parsed section is a tailored waypoint reference
    // if so extract the course/distance suffix from the fix identifier
    String s = section.matches(tailored().pattern())
        ? section.substring(0, section.length() - 6)
        : section;

    return lookupService
        .apply(s).stream()
        .map(fix -> section.equals(s)
            ? new FixElement(fix, sectionSplit.wildcards())
            : new TailoredElement(fix, section, sectionSplit.wildcards()))

        .collect(Collectors.toList());
  }
}
