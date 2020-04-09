package org.mitre.tdp.boogie.conformance.alg.assign;

import org.mitre.tdp.boogie.conformance.ConformablePoint;
import org.mitre.tdp.boogie.conformance.model.ConsecutiveLegs;

/**
 * A {@link LegAssigner} takes a conformable point and returns a {@link ConsecutiveLegs} representing
 * the pairing of a time with a leg - indicating at that time the aircraft was best conforming to that
 * particular leg.
 */
@FunctionalInterface
public interface LegAssigner {

  /**
   * Returns the legs the aircraft is most likely on at the given point.
   */
  ConsecutiveLegs assignmentFor(ConformablePoint conformablePoint);
}
