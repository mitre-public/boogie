package org.mitre.tdp.boogie.alg.resolve;

import java.util.Collections;
import java.util.List;

import org.mitre.tdp.boogie.alg.split.RouteToken;

final class LatLonResolver implements SingleTokenResolver {

  LatLonResolver() {
  }

  @Override
  public List<ResolvedToken> resolve(RouteToken sectionSplit) {
    return CoordinateFormatStandard.supported(sectionSplit.infrastructureName()) ?
        Collections.singletonList(LatLonToken.from(sectionSplit)) : Collections.emptyList();
  }
}
