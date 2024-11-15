package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class TestMagneticVariation {

  private static final MagneticVariation parser = new MagneticVariation();

  @Test
  void testParserFiltersEmptyString() {
    assertEquals(Optional.empty(), parser.apply(""));
  }

  @Test
  void testParserRemovesWhitespace() {
    assertEquals(Optional.empty(), parser.apply("     "));
  }

  @Test
  void testParserFiltersNonNumericSubstringsAfterStart() {
    assertEquals(Optional.empty(), parser.apply("E013A"));
  }

  @Test
  void testParserOnlyAllows_E_W_Variations() {
    assertEquals(Optional.empty(), parser.apply("A0140"));
  }

  @Test
  void testParserFiltersTrueNorthVariations() {
    assertEquals(Optional.empty(), parser.apply("T0140"));
  }

  @Test
  void testParserReturnsCorrectVariationValues() {
    assertAll(
        () -> assertEquals(Optional.of(14.), parser.apply("E0140")),
        () -> assertEquals(Optional.of(-14.), parser.apply("W0140"))
    );
  }
}
