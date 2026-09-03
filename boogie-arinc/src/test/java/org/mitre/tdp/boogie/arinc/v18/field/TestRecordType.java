package org.mitre.tdp.boogie.arinc.v18.field;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TestRecordType {

  @Test
  void testParseGoodRecordType() {
    assertEquals(Optional.of(RecordType.T), RecordType.SPEC.apply("T"));
  }

  @Test
  void testEmptyOnBadRecordType() {
    assertEquals(Optional.empty(), RecordType.SPEC.apply("A"));
  }

  @Test
  void testRangeParsingMatchesStringParsing() {
    assertAll(
        () -> assertRangeEquals("S"),
        () -> assertRangeEquals("T"),
        () -> assertRangeEquals("A"),
        () -> assertRangeEquals(""),
        () -> assertRangeEquals("ST"),
        () -> assertThrows(IndexOutOfBoundsException.class, () -> RecordType.SPEC.parse("S", -1, 1)),
        () -> assertThrows(IndexOutOfBoundsException.class, () -> RecordType.SPEC.parse("S", 0, 2)),
        () -> assertThrows(IndexOutOfBoundsException.class, () -> RecordType.SPEC.parse("S", 1, 0)),
        () -> assertThrows(NullPointerException.class, () -> RecordType.SPEC.apply(null)),
        () -> assertThrows(NullPointerException.class, () -> RecordType.SPEC.parse(null, 0, 0))
    );
  }

  private static void assertRangeEquals(String fieldValue) {
    String source = "prefix" + fieldValue + "suffix";
    int startOffset = "prefix".length();
    assertEquals(
        RecordType.SPEC.apply(fieldValue),
        RecordType.SPEC.parse(source, startOffset, startOffset + fieldValue.length()));
  }
}
