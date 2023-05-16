package org.mitre.tdp.boogie.alg;

import java.util.Optional;
import javax.annotation.Nullable;

@FunctionalInterface
public interface Expander {

  Optional<ExpandedRoute> expand(String route, RouteContext context);

//  default Optional<ExpandedRoute> expand(String route) {
//    return expand(route, null, null);
//  }
//
//  default Optional<ExpandedRoute> expand(String route, @Nullable String departureRunway, @Nullable String arrivalRunway) {
//
//  }
//
//  default Optional<ExpandedRoute> expand(String route)
}
