package org.mitre.tdp.boogie.conformance.alg.assemble;

import java.util.Optional;
import javax.annotation.Nonnull;

import org.mitre.tdp.boogie.Leg;

public class LegTriple implements ConsecutiveLegs {

  private final Leg previous;
  private final Leg current;
  private final Leg next;

  private Object sourceObject;

  /**
   * Generates a new simple tuple of {@link ConsecutiveLegs}.
   */
  public LegTriple(Leg p, @Nonnull Leg c, Leg n) {
    this.previous = p;
    this.current = c;
    this.next = n;
  }

  @Override
  public Optional<Leg> previous() {
    return Optional.ofNullable(previous);
  }

  @Override
  public Leg current() {
    return current;
  }

  @Override
  public Optional<Leg> next() {
    return Optional.ofNullable(next);
  }

  public LegTriple swap() {
    return new LegTriple(next, current, previous);
  }

  @Override
  public Optional<Object> getSourceObject() {
    return Optional.ofNullable(sourceObject);
  }

  @Override
  public LegTriple setSourceObject(Object o) {
    this.sourceObject = o;
    return this;
  }
}
