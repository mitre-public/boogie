package org.mitre.tdp.boogie.alg.resolve;

import java.util.Collections;
import java.util.List;

import org.mitre.tdp.boogie.alg.split.SectionSplit;

public final class LatLonResolver implements SingleSplitSectionResolver {

  @Override
  public List<ResolvedElement> resolve(SectionSplit sectionSplit) {
    return CoordinateFormatStandard.supported(sectionSplit.value()) ?
        Collections.singletonList(LatLonElement.from(sectionSplit)) : Collections.emptyList();
  }
}
