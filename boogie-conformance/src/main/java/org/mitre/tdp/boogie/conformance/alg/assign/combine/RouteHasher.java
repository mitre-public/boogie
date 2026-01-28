package org.mitre.tdp.boogie.conformance.alg.assign.combine;

import java.util.function.Function;

import org.apache.commons.lang3.builder.HashCodeBuilder;
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
    HashCodeBuilder builder = new HashCodeBuilder();
    flyableLeg.routes().stream()
        .map(Route::source)
        .map(Object::hashCode)
        .forEach(builder::append);
    return builder.toHashCode();
  }
}
