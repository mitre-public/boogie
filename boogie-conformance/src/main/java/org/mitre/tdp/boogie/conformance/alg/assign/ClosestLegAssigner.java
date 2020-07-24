package org.mitre.tdp.boogie.conformance.alg.assign;

import org.mitre.caasd.commons.LatLong;
import org.mitre.caasd.commons.collect.MetricTree;
import org.mitre.caasd.commons.collect.SearchResult;
import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.conformance.alg.assemble.FlyableLeg;

/**
 * A simple {@link LegAssigner} which assigns the point to the closes available leg.
 */
public interface ClosestLegAssigner extends LegAssigner {

  /**
   * Returns the {@link MetricTree} structure containing the legs to assign the procedure to.
   */
  MetricTree<LatLong, FlyableLeg> legIndex();

  @Override
  default FlyableLeg assignmentFor(ConformablePoint point) {
    SearchResult<LatLong, FlyableLeg> closest = legIndex().getClosest(point.latLong());
    return closest.value();
  }
}
