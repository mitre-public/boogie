package org.mitre.tdp.boogie.conformance.alg.assemble;

import java.util.Optional;
import javax.annotation.Nonnull;

import org.mitre.tdp.boogie.Leg;

/**
 * Simple interface for a pair of concrete leg types.
 */
public class LegPair implements ConsecutiveLegs {

  /**
   * The previous concrete leg.
   */
  private final Leg previous;
  /**
   * The current concrete leg.
   */
  private final Leg current;
  /**
   * Returns whether the given leg pair is skipping a non-concrete leg type.
   */
  private final boolean isSkip;

  private Object sourceObject;

  public LegPair(@Nonnull Leg previous, @Nonnull Leg current, boolean isSkip) {
    this.previous = previous;
    this.current = current;
    this.isSkip = isSkip;
  }

  @Override
  public Optional<Leg> previous() {
    return Optional.of(previous);
  }

  @Override
  public Leg current() {
    return current;
  }

  @Override
  public Optional<Leg> next() {
    return Optional.empty();
  }

  public boolean isSkip() {
    return isSkip;
  }

  public LegPair swap() {
    return new LegPair(current, previous, isSkip);
  }

  @Override
  public Optional<Object> getSourceObject() {
    return Optional.ofNullable(sourceObject);
  }

  @Override
  public LegPair setSourceObject(Object o) {
    this.sourceObject = o;
    return this;
  }
}
