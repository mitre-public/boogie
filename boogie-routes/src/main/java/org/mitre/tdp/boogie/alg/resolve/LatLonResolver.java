package org.mitre.tdp.boogie.alg.resolve;

import java.util.Collections;
import java.util.List;

import org.mitre.tdp.boogie.alg.split.SectionSplit;

public final class LatLonResolver implements SingleSplitSectionResolver {

  @Override
  public List<ResolvedElement> resolve(SectionSplit sectionSplit) {
    String section = sectionSplit.value();
    boolean match = section.matches(SectionHeuristics.LATLON.pattern());
    return match ? Collections.singletonList(LatLonElement.from(section, sectionSplit.wildcards())) : Collections.emptyList();
  }
}
