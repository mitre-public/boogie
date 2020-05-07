package org.mitre.tdp.boogie.conformance.alg.assign;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import org.mitre.tdp.boogie.conformance.ConformablePoint;
import org.mitre.tdp.boogie.conformance.model.ConsecutiveLegs;

/**
 * An interface for a type of {@link LegAssigner} which pre-computes the leg assignments and then serves them
 * on a point-wise basis.
 *
 * <p>There is a class of leg assignment algorithms which allow refined results to be
 */
public interface PrecomputedAssigner extends Serializable {

  /**
   * Returns the map of {@link ConformablePoint} to their assigned {@link ConsecutiveLegs} based on the input collection
   * of all points and legs.
   */
  Map<ConformablePoint, ConsecutiveLegs> assignments(Collection<ConformablePoint> allPoints, Collection<ConsecutiveLegs> allLegs);
}
