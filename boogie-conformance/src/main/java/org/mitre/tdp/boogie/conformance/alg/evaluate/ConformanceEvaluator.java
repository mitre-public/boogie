package org.mitre.tdp.boogie.conformance.alg.evaluate;

import java.util.Optional;

import org.mitre.caasd.commons.Distance;
import org.mitre.caasd.commons.Spherical;
import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.conformance.alg.assemble.LegPair;
import org.mitre.tdp.boogie.conformance.alg.assign.LegAssigner;

/**
 * A conformance evaluator takes a {@link LegAssigner} which can be used to find the leg the aircraft is flying at the time
 * of the point and uses it to decide as a simple y/n whether the aircraft is conforming to the assigned leg.
 */
@FunctionalInterface
public interface ConformanceEvaluator {

  /**
   * Y/N to whether the current point is conforming to its assigned leg.
   */
  Optional<Boolean> conforming(ConformablePoint point, LegPair consecutiveLegs);
}
