package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class TestMinimumAltitude {

  private static final MinimumAltitude parser = new MinimumAltitude();

  @Test
  void testParserFiltersEmptyString() {
    assertEquals(Optional.empty(), parser.apply(""));
  }

  @Test
  void testParserRemovesWhitespace() {
    assertEquals(Optional.empty(), parser.apply("     "));
  }

  @Test
  void testParserFiltersNonNumericFlightLevel() {
    assertEquals(Optional.empty(), parser.apply("FLAVA"));
  }

  @Test
  void testParserFiltersNonNumericFeet() {
    assertEquals(Optional.empty(), parser.apply("1234A"));
  }

  @Test
  void testParserFiltersUNKNN() {
    assertEquals(Optional.empty(), parser.apply("UNKNN"));
  }

  /**
   * For now - this may be updated at a later date.
   */
  @Test
  void testParserFiltersNESTB() {
    assertEquals(Optional.empty(), parser.apply("NESTB"));
  }

  @Test
  void testParserReturnsValidMinimumAltitudes() {
    assertAll(
        () -> assertEquals(Optional.of(5000.), parser.apply("05000")),
        () -> assertEquals(Optional.of(5000.), parser.apply("FL050")),
        () -> assertEquals(Optional.of(-12.), parser.apply("-0012")),
        () -> assertEquals(Optional.of(29000.), parser.apply("29000"))
    );
  }
}