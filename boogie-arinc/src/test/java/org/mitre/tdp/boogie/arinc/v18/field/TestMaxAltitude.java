package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class TestMaxAltitude {

  private static final MaxAltitude parser = new MaxAltitude();

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

  /**
   * For now - this may be updated at a later date.
   */
  @Test
  void testParserFiltersUNLTD() {
    assertEquals(Optional.empty(), parser.apply("UNLTD"));
  }

  @Test
  void testParserReturnsValidMaxAltitudesInFeet() {
    assertAll(
        () -> assertEquals(Optional.of(18000.), parser.apply("FL180")),
        () -> assertEquals(Optional.of(18000.), parser.apply("18000"))
    );
  }
}
