package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class TestVerticalAngle {

  private static final VerticalAngle parser = new VerticalAngle();

  @Test
  void testParseValidAngle() {
    assertEquals(Optional.of(-9.69), parser.apply("-969"));
  }

  @Test
  void testParsesZeroVerticalAngle() {
    assertEquals(Optional.of(.0d), parser.apply(" 000"));
  }

  @Test
  void testParseExceptionAngleTooHigh() {
    assertEquals(Optional.empty(), parser.apply("-1045"));
  }

  @Test
  void testParseExceptionBadAngle() {
    assertEquals(Optional.empty(), parser.apply("-12A"));
  }

  @Test
  void appliesStrictAngleLimit() {
    assertAll(
        () -> assertEquals(Optional.of(9.99), parser.apply("0999")),
        () -> assertEquals(Optional.of(-9.99), parser.apply("-999")),
        () -> assertEquals(Optional.empty(), parser.apply("1000")),
        () -> assertEquals(Optional.empty(), parser.apply("-1000"))
    );
  }
}
