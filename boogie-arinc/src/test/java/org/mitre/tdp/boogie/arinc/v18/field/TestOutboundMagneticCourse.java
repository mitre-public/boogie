package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;

public class TestOutboundMagneticCourse {

  private static final OutboundMagneticCourse parser = new OutboundMagneticCourse();

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
    assertEquals(Optional.empty(), parser.apply("ACB1"));
  }

  @Test
  void testParserReturnsTrueCourseInputsInWholeDegrees() {
    assertAll(
        () -> assertEquals(Optional.of(123.0), parser.apply("123T")),
        () -> assertEquals(Optional.of(350.0), parser.apply("350T"))
    );
  }

  @Test
  void testParserReturnsValidDoublesIfPresent() {
    assertAll(
        () -> assertEquals(Optional.of(123.4), parser.apply("1234")),
        () -> assertEquals(Optional.of(.1), parser.apply("0001")),
        () -> assertEquals(Optional.of(40.), parser.apply("0400"))
    );
  }
}
