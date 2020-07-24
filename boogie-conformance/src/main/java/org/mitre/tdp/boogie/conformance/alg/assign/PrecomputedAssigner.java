package org.mitre.tdp.boogie.conformance.alg.assign;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.conformance.alg.assemble.FlyableLeg;

/**
 * An interface for a type of {@link LegAssigner} which pre-computes the leg assignments and then serves them
 * on a point-wise basis.
 *
 * <p>There is a class of leg assignment algorithms which allow refined results to be
 */
@FunctionalInterface
public interface PrecomputedAssigner extends Serializable {

  /**
   * Returns the map of {@link ConformablePoint} to their assigned {@link FlyableLeg} based on the input collection
   * of all points and legs.
   */
  Map<ConformablePoint, FlyableLeg> assignments(Collection<? extends ConformablePoint> allPoints, Collection<? extends FlyableLeg> allLegs);
}
