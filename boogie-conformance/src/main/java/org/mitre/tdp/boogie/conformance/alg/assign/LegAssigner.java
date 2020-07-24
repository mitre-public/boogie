package org.mitre.tdp.boogie.conformance.alg.assign;

import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.conformance.alg.assemble.FlyableLeg;

/**
 * A {@link LegAssigner} takes a conformable point and returns a {@link FlyableLeg} representing the pairing of a time
 * with a leg - indicating at that time the aircraft was best conforming to that particular leg.
 */
@FunctionalInterface
public interface LegAssigner {

  /**
   * Returns the legs the aircraft is most likely flying at the given point.
   */
  FlyableLeg assignmentFor(ConformablePoint conformablePoint);
}
