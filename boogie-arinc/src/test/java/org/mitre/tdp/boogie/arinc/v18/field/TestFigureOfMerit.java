package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class TestFigureOfMerit {

  private static final FigureOfMerit parser = new FigureOfMerit();

  @Test
  void testParserFiltersEmptyInputs() {
    assertEquals(Optional.empty(), parser.apply(""));
  }

  @Test
  void testParserFiltersWhitespaceInputs() {
    assertEquals(Optional.empty(), parser.apply("   "));
  }

  @Test
  void testParserFiltersNonNumericInputs() {
    assertEquals(Optional.empty(), parser.apply("HI"));
  }

  @Test
  void testParserReturnsValidDoubleIfPresent() {
    assertAll(
        () -> assertEquals(Optional.of(-150), parser.apply("-150")),
        () -> assertEquals(Optional.of(150), parser.apply(" 150")),
        () -> assertEquals(Optional.of(150), parser.apply("+150")),
        () -> assertEquals(Optional.of(-150), parser.parse("xx -150 yy", 2, 7))
    );
  }

  @Test
  void testRangeAndOverflowEdgeCases() {
    assertEquals(Optional.empty(), parser.apply("-"));
    assertThrows(NumberFormatException.class, () -> parser.apply("2147483648"));
    assertThrows(IndexOutOfBoundsException.class, () -> parser.parse("123", 2, 1));
  }
}
