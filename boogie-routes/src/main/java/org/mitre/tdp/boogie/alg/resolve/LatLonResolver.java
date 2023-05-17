package org.mitre.tdp.boogie.alg.resolve;

import java.util.Collections;
import java.util.List;

import org.mitre.tdp.boogie.alg.split.RouteToken;

final class LatLonResolver implements SingleSplitSectionResolver {

  LatLonResolver() {
  }

  @Override
  public List<ResolvedElement> resolve(RouteToken sectionSplit) {
    return CoordinateFormatStandard.supported(sectionSplit.infrastructureName()) ?
        Collections.singletonList(LatLonElement.from(sectionSplit)) : Collections.emptyList();
  }
}
