package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.v18.field.RouteType;

public class TestRouteType {

  @Test
  public void testFiltersTrimmableInputs() {
    assertTrue(new RouteType().filterInput("  "));
  }
}
