package org.mitre.tdp.boogie.conformance.alg.assign;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.Objects;

import org.mitre.tdp.boogie.Leg;

/**
 * Simple decorator class for a source of {@link Leg}s which can be composed into {@link FlyableLeg} elements (i.e. has some
 * assumed structure - rather than being a free-form object).
 */
public final class Route {

  /**
   * Functional to apply to the source to extract a consecutive sequence of legs.
   */
  private final List<Leg> legs;
  /**
   * The source object of the legs.
   */
  private final Object source;

  /**
   * The precomputed hash of the route object - turns out it can be expensive to continually be computing this value and in
   * reality it's constant over the lifetime of the object - so we compute it once and save it.
   */
  private final int hash;

  private Route(List<Leg> legs, Object source) {
    this.legs = legs;
    this.source = source;
    this.hash = Objects.hash(legs, source);
  }

  public Object source() {
    return source;
  }

  public List<Leg> legs() {
    return legs;
  }

  public static Route newRoute(List<Leg> legs, Object source) {
    return new Route(legs, checkNotNull(source));
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass())
      return false;
    Route route = (Route) o;
    return hash == route.hash && Objects.equals(legs, route.legs) && Objects.equals(source, route.source);
  }

  @Override
  public int hashCode() {
    return hash;
  }
}
