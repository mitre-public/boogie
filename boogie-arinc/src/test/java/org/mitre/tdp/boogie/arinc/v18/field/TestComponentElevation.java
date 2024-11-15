package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class TestComponentElevation {

  private static final ComponentElevation parser = new ComponentElevation();

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
        () -> assertEquals(Optional.of(-150.0), parser.apply("-150")),
        () -> assertEquals(Optional.of(150.0), parser.apply(" 150")),
        () -> assertEquals(Optional.of(150.0), parser.apply("+150"))
    );
  }
}
