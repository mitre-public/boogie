package org.mitre.tdp.boogie.arinc.v18.field;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

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

  @Test
  void testEmbeddedSlicesMatchStringParsingAndFilterByPosition() {
    assertAll(
        () -> assertEquals(parser.apply("ABCD"), parser.parse("xxABCDyy", 2, 6)),
        () -> assertEquals(parser.apply("AYCD"), parser.parse("xxAYCDyy", 2, 6)),
        () -> assertEquals(parser.apply("AVCD"), parser.parse("xxAVCDyy", 2, 6)),
        () -> assertEquals(parser.apply("IAFZ"), parser.parse("xxIAFZyy", 2, 6)),
        () -> assertEquals(Optional.of("A CD"), parser.parse("xxAVCDyy", 2, 6)),
        () -> assertEquals(Optional.of("    "), parser.parse("xxIAFZyy", 2, 6))
    );
  }

  @Test
  void testShortEmbeddedSliceMatchesMalformedStringBehavior() {
    assertAll(
        () -> assertThrows(StringIndexOutOfBoundsException.class, () -> parser.apply("ABC")),
        () -> assertThrows(StringIndexOutOfBoundsException.class, () -> parser.parse("xxABCyy", 2, 5))
    );
  }

  @Test
  void testFilteredDescriptionsAreCached() {
    Optional<String> direct = parser.apply("ABCD");
    Optional<String> ranged = parser.parse("xxABCDyy", 2, 6);
    Optional<String> normalized = parser.parse("xxIAFZyy", 2, 6);

    assertAll(
        () -> assertSame(direct, ranged),
        () -> assertSame(direct.orElseThrow(), ranged.orElseThrow()),
        () -> assertSame(parser.apply("    "), normalized),
        () -> assertSame(parser.apply("    ").orElseThrow(), normalized.orElseThrow())
    );
  }
}
