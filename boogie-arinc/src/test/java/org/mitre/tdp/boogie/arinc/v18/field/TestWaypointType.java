package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class TestWaypointType {

  private static final WaypointType parser = new WaypointType();

  @Test
  void testParserWithValidCodes() {
    assertAll(
        () -> assertEquals(Optional.of("R  "), parser.apply("R  ")),
        () -> assertEquals(Optional.of(" A "), parser.apply(" A ")),
        () -> assertEquals(Optional.of("  Z"), parser.apply("  Z"))
    );
  }

  @Test
  void testParserWithInvalidCodes() {
    assertAll(
        () -> assertEquals(Optional.of("   "), parser.apply("B  ")),
        () -> assertEquals(Optional.of("   "), parser.apply(" Z ")),
        () -> assertEquals(Optional.of("   "), parser.apply("  A"))
    );
  }
}
