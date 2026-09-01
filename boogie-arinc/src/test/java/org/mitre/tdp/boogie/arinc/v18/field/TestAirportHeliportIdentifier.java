package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class TestAirportHeliportIdentifier {

  private static final AirportHeliportIdentifier parser = new AirportHeliportIdentifier();

  @Test
  void testParserFiltersEmptyInputs() {
    assertEquals(Optional.empty(), parser.apply(""));
  }

  @Test
  void testParserFiltersWhitespaceInputs() {
    assertEquals(Optional.empty(), parser.apply("   "));
  }

  @Test
  void testParserReturnsNonEmptyInput() {
    assertEquals(Optional.of("HI"), parser.apply("HI"));
  }

  @Test
  void testParserReturnsTrimmedInput() {
    assertAll(
        () -> assertEquals(Optional.of("HI"), parser.apply("   HI   ")),
        () -> assertEquals(Optional.of("HI"), parser.parse("xx  HI  yy", 2, 8)),
        () -> assertEquals(Optional.of("\u2003"), parser.apply("\u2003")),
        () -> assertThrows(IndexOutOfBoundsException.class, () -> parser.parse("HI", -1, 2)),
        () -> assertThrows(IndexOutOfBoundsException.class, () -> parser.parse("HI", 1, 0)),
        () -> assertThrows(NullPointerException.class, () -> parser.parse(null, 0, 0))
    );
  }
}
