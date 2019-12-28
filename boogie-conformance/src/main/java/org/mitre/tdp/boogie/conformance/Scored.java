package org.mitre.tdp.boogie.conformance;

import java.io.Serializable;


/**
 * The hybrid input and output object for the {@link Conformance} class.
 *
 * The scored class decorates a {@link Scorable} object for input into
 * conformance, but also provides a mutable locations for the
 */
public class Scored<S extends Scorable> implements Serializable, Scorable {
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
   *
   * In that context these scores represent the times at which the wrapped object was
   * the optimal choice as well as the along/cross track scores at that point.
   */
  private Scores scores;

  @Deprecated
  public Scored() {
    this.scorable = null;
    this.index = 0;
    this.scores = null;
  }

  public Scored(S base) {
    this.scorable = base;
    this.index = 0;
    this.scores = null;
  }

  public S scorable() {
    return scorable;
  }

  public int index() {
    return index;
  }

  public Scored<S> setIndex(int idx) {
    this.index = idx;
    return this;
  }

  public Scores scores() {
    return scores;
  }

  public Scored<S> setScores(Scores scores) {
    this.scores = scores;
    return this;
  }

  public boolean scored() {
    return scores != null;
  }

  @Override
  public Scorer scorer() {
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
