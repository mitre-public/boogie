package org.mitre.tdp.boogie.conformance;

/**
 * A custom scorer typically built on the fly by {@link Scorable} objects.
 */
public interface Scorer<U> {

  /**
   * Returns a score indicating the likelyhood that the target object is associated with
   * the {@link Scorable} which created the scorer.
   */
  double score(U that);

  double transitionScore(Scorable<U> l2);
}
