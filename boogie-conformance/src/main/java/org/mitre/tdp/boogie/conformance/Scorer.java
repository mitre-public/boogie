package org.mitre.tdp.boogie.conformance;

import java.util.Optional;

/**
 * A custom scorer typically built on the fly by {@link Scorable} objects.
 */
@FunctionalInterface
public interface Scorer<U, S extends Scorable<U, S>> {

  /**
   * Returns a score indicating the likelyhood that the target object is associated with
   * the {@link Scorable} which created the scorer.
   */
  Optional<Double> score(U that);
}
