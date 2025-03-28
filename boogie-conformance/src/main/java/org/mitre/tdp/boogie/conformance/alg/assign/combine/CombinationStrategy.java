package org.mitre.tdp.boogie.conformance.alg.assign.combine;

import java.util.Collection;
import java.util.Map;

import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.conformance.alg.assign.RouteAssigner;
import org.mitre.tdp.boogie.conformance.alg.assign.link.LinkingStrategy;

/**
 * A combination strategy defines a methodology for combining {@link FlyableLeg}s which represent logically similar flight
 * paths/trajectories into {@link CompositeLeg}s. A composite leg then elects a representative which can be used in place
 * of the full set of member legs within the {@link RouteAssigner} for scoring, etc.
 */
@FunctionalInterface
public interface CombinationStrategy {

  /**
   * Takes a collection of input {@link FlyableLeg}s and reduces it them to an output mapping from {@code leg->representative}.
   *
   * <p>This signature is chosen so that the results of the {@link LinkingStrategy} can be done between candidates from the pre
   * combination phase and then mapped to the appropriate post-combination legs.
   */
  Map<FlyableLeg, CompositeLeg> combineSimilar(Collection<FlyableLeg> flyableLegs);
}
