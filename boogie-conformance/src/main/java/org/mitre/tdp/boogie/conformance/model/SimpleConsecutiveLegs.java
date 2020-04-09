package org.mitre.tdp.boogie.conformance.model;

import java.util.Optional;

import org.mitre.tdp.boogie.Leg;

public class SimpleConsecutiveLegs implements ConsecutiveLegs {

  private final Leg previous;
  private final Leg current;
  private final Leg next;

  public SimpleConsecutiveLegs(Leg p, Leg c, Leg n) {
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
}
