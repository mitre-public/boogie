package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class TestRouteType {

  private static final RouteType parser = new RouteType();

  @Test
  void testFiltersTrimmableInputs() {
    assertEquals(Optional.empty(), parser.apply("  "));
  }
}
