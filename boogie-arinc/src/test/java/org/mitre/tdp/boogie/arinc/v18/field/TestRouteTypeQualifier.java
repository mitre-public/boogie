package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class TestRouteTypeQualifier {

  private static final RouteTypeQualifier parser = new RouteTypeQualifier();

  @Test
  void testParserFiltersEmptyInputs() {
    assertEquals(Optional.empty(), parser.apply(""));
  }

  @Test
  void testParserFiltersWhitespaceInputs() {
    assertEquals(Optional.empty(), parser.apply(" "));
  }

  @Test
  void testParserPassesAllowedCodes() {
    RouteTypeQualifier.allowedCodes.forEach(code -> assertEquals(Optional.of(code), parser.apply(code)));
  }

  @Test
  void testParserFiltersDisallowedCodes() {
    assertFalse(RouteTypeQualifier.allowedCodes.contains("@"));
    assertEquals(Optional.empty(), parser.apply("@"));
  }
}
