package org.mitre.tdp.boogie.conformance.alg.assemble;

import java.util.Optional;
import javax.annotation.Nonnull;

import org.mitre.tdp.boogie.Leg;

public class TemplateLegPair<L extends Leg> implements LegPair {
  /**
   * The previous concrete leg.
   */
  private final L previous;
  /**
   * The current concrete leg.
   */
  private final L current;

  private Object sourceObject;

  public TemplateLegPair(@Nonnull L previous, @Nonnull L current) {
    this.previous = previous;
    this.current = current;
  }

  @Override
  public L previous() {
    return previous;
  }

  @Override
  public L current() {
    return current;
  }

  @Override
  public Optional<Object> getSourceObject() {
    return Optional.ofNullable(sourceObject);
  }

  @Override
  public TemplateLegPair<L> setSourceObject(Object o) {
    this.sourceObject = o;
    return this;
  }
}
