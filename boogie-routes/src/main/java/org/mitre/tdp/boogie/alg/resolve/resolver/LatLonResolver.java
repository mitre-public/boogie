package org.mitre.tdp.boogie.alg.resolve.resolver;

import java.util.Collections;
import java.util.List;

import org.mitre.tdp.boogie.alg.resolve.SectionHeuristics;
import org.mitre.tdp.boogie.alg.resolve.element.LatLonElement;
import org.mitre.tdp.boogie.alg.resolve.element.ResolvedElement;
import org.mitre.tdp.boogie.alg.split.SectionSplit;

public final class LatLonResolver implements SingleSplitSectionResolver {

  @Override
  public List<ResolvedElement<?>> resolve(SectionSplit sectionSplit) {
    String section = sectionSplit.value();
    boolean match = section.matches(SectionHeuristics.latLon().pattern());
    return match ? Collections.singletonList(LatLonElement.from(section, sectionSplit.wildcards())) : Collections.emptyList();
  }
}
