package org.mitre.tdp.boogie.conformance.alg.assign;

import org.mitre.caasd.commons.LatLong;
import org.mitre.caasd.commons.collect.MetricTree;
import org.mitre.caasd.commons.collect.SearchResult;
import org.mitre.tdp.boogie.conformance.ConformablePoint;
import org.mitre.tdp.boogie.conformance.model.ConsecutiveLegs;

/**
 * A simple {@link LegAssigner} which assigns the
 */
public interface ClosestLegAssigner extends LegAssigner {

  MetricTree<LatLong, ConsecutiveLegs> legIndex();

  @Override
  default ConsecutiveLegs assignmentFor(ConformablePoint point) {
    SearchResult<LatLong, ConsecutiveLegs> closest = legIndex().getClosest(point.latLong());
    return closest.value();
  }
}
