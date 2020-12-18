package org.mitre.tdp.boogie.conformance.alg.evaluate;

import java.util.Optional;

import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.conformance.alg.LegPair;

/**
 * A conformance evaluator takes a {@link ConformablePoint} and {@link LegPair} being flown at the time
 * of the point and determines whether the aircraft is conforming to the assigned leg.
 */
@FunctionalInterface
public interface ConformanceEvaluator {

  /**
   * Y/N to whether the current point is conforming to its assigned leg.
   */
  Optional<Boolean> conforming(ConformablePoint point, LegPair consecutiveLegs);
}
