package org.mitre.tdp.boogie.conformance.alg.assign.combine;

import java.util.function.Function;

import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.conformance.alg.assign.Route;

/**
 * This class provides a hash based on the Flyable Leg's Single Source.
 */
public final class RouteHasher implements Function<FlyableLeg, Integer> {
  private RouteHasher() {}
  public static RouteHasher newInstance() {
    return new RouteHasher();
  }
  @Override
  public Integer apply(FlyableLeg flyableLeg) {
    return flyableLeg.routes().stream()
        .findFirst()
        .map(Route::source)
        .map(Object::hashCode)
        .orElseThrow();
  }
}
