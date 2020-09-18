package org.mitre.tdp.boogie.conformance.alg.assemble;

import java.util.Optional;

import org.mitre.tdp.boogie.Leg;

/**
 * Default concrete implementation of a {@link LegPair}.
 */
public class LegPairImpl implements LegPair {
  /**
   * The previous concrete leg.
   */
  private final Leg previous;
  /**
   * The current concrete leg.
   */
  private final Leg current;

  private Object sourceObject;

  public LegPairImpl(Leg previous, Leg current) {
    this.previous = previous;
    this.current = current;
  }

  @Override
  public Leg previous() {
    return previous;
  }

  @Override
  public Leg current() {
    return current;
  }

  @Override
  public Optional<Object> getSourceObject() {
    return Optional.ofNullable(sourceObject);
  }

  @Override
  public LegPairImpl setSourceObject(Object o) {
    this.sourceObject = o;
    return this;
  }
}
