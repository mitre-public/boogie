package org.mitre.tdp.boogie.v18.field;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.v18.spec.field.RouteIdentifier;

public class TestRouteIdentifier {

  @Test
  public void testFiltersTrimmableInput() {
    assertTrue(RouteIdentifier.enroute().filterInput("   "));
  }
}
