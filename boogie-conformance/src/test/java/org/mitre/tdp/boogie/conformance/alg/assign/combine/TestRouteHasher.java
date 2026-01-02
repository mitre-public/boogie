package org.mitre.tdp.boogie.conformance.alg.assign.combine;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.conformance.alg.assign.Route;

public class TestRouteHasher {
  @Test
  void hash() {
    List<Leg> legs = List.of();
    Route<String> route = Route.newRoute(legs, "SOURCE");
    Route<String> route2 = Route.newRoute(legs, "SOURCE_2");
    assertNotEquals(route.hashCode(), route2.hashCode());
  }
}
