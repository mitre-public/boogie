package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class TestWaypointDescription {

  private static final WaypointDescription parser = new WaypointDescription();

  @Test
  void testParsing() {
    assertAll(
        () -> assertEquals(Optional.of("ABCD"), parser.apply("ABCD"), "Valid codes should be passed through in position."),
        () -> assertEquals(Optional.of("AYCD"), parser.apply("AYCD"), "Valid codes should be passed through in position."),
        () -> assertEquals(Optional.of("A CD"), parser.apply("AVCD"), "Valid codes should be passed through in position."),
        () -> assertEquals(Optional.of("    "), parser.apply("IAFZ"), "Invalid codes should be replaced with empty.")
    );
  }
}
