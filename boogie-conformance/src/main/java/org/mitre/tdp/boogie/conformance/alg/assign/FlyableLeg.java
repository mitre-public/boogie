package org.mitre.tdp.boogie.conformance.alg.assign;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import javax.annotation.Nullable;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.Route;

public final class FlyableLeg {

  /**
   * The leg previous to the current leg of the route - given the current leg is not the initial (in which case this is null).
   */
  private final Leg previous;
  /**
   * The current leg of the route being flown.
   */
  private final Leg current;
  /**
   * The next leg after the current leg of the route - given the current leg is not the final (in which case this is null).
   */
  private final Leg next;
  /**
   * The source {@link Route}(s) which reference this flyable leg.
   */
  private final LinkedHashSet<Route> routes;

  public FlyableLeg(
      @Nullable Leg previous,
      Leg current,
      @Nullable Leg next,
      Route... routes) {
    this(previous, current, next, new LinkedHashSet<>(Arrays.asList(routes)));
  }

  public FlyableLeg(
      @Nullable Leg previous,
      Leg current,
      @Nullable Leg next,
      LinkedHashSet<Route> routes) {
    this.previous = previous;
    this.current = checkNotNull(current);
    this.next = next;
    this.routes = checkNotNull(routes);
  }

  public Optional<Leg> previous() {
    return Optional.ofNullable(previous);
  }

  public <T> Optional<T> previous(Function<Leg, Optional<T>> ext) {
    return previous().flatMap(ext);
  }

  public Leg current() {
    return current;
  }

  public Optional<Leg> next() {
    return Optional.ofNullable(next);
  }

  public <T> Optional<T> next(Function<Leg, Optional<T>> ext) {
    return next().flatMap(ext);
  }

  public LinkedHashSet<Route> routes() {
    return routes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FlyableLeg that = (FlyableLeg) o;
    return Objects.equals(previous, that.previous) &&
        Objects.equals(current, that.current) &&
        Objects.equals(next, that.next) &&
        Objects.equals(routes, that.routes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(previous, current, next, routes);
  }

  @Override
  public String toString() {
    return "FlyableLeg{" +
        "previous=" + previous +
        ", current=" + current +
        ", next=" + next +
        '}';
  }
}
