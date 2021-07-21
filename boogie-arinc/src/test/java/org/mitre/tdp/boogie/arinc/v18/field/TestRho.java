package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class TestRho {

  private static final Rho parser = new Rho();

  @Test
  void testParserFiltersEmptyInputs() {
    assertEquals(Optional.empty(), parser.apply(""));
  }

  @Test
  void testParserFiltersWhitespaceInputs() {
    assertEquals(Optional.empty(), parser.apply("    "));
  }

  @Test
  void testParserFiltersNonNumericInputs() {
    assertEquals(Optional.empty(), parser.apply("A11A"));
  }

  @Test
  void testParserReturnsValidDoublesIfPresent() {
    assertAll(
        () -> assertEquals(Optional.of(9.1), parser.apply("0091")),
        () -> assertEquals(Optional.of(.1), parser.apply("0001")),
        () -> assertEquals(Optional.of(106.), parser.apply("1060"))
    );
  }
}
