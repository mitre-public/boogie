package org.mitre.tdp.boogie.conformance;

import org.mitre.tdp.boogie.conformance.alg.assign.dp.TimeBasedScoreMaximizer;

/**
 * The hybrid input and output object for the {@link TimeBasedScoreMaximizer} class.
 * <p>
 * The scored class decorates a {@link Scorable} object for input into
 * conformance, but also provides a mutable locations for the
 */
public class Scored<U, S extends Scorable<U, S>> implements Scorable<U, S> {
  /**
   * The internal {@link Scorable} object.
   */
  private final S scorable;
  /**
   * The internal index of the scored object, this is used for reference within
   * the conformance algorithm to associate the appropriate scores with the object
   * without having to maintain multiple copies etc.
   */
  private int index;
  /**
   * Return the set of scores associated with the object after having been passed
   * through the conformance algorithm.
   * <p>
   * In that context these scores represent the times at which the wrapped object was
   * the optimal choice as well as the along/cross track scores at that point.
   */
  private Scores associatedScores;

  public Scored(S base) {
    this.scorable = base;
    this.index = 0;
    this.associatedScores = null;
  }

  public S scorable() {
    return scorable;
  }

  public int index() {
    return index;
  }

  public Scored<U, S> setIndex(int idx) {
    this.index = idx;
    return this;
  }

  public Scores associatedScores() {
    return associatedScores;
  }

  public Scored<U, S> setAssociatedScores(Scores associatedScores) {
    this.associatedScores = associatedScores;
    return this;
  }

  public boolean scored() {
    return associatedScores != null;
  }

  @Override
  public Scorer<U, S> scorer() {
    return scorable().scorer();
  }

  @Override
  public boolean equals(Object that) {
    return that instanceof Scored && scorable().equals(that);
  }

  @Override
  public int hashCode() {
    return scorable().hashCode();
  }
}
