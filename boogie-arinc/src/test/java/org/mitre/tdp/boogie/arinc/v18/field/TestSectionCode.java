package org.mitre.tdp.boogie.arinc.v18.field;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TestSectionCode {

  @Test
  void testValidSectionCode() {
    assertEquals(Optional.of(SectionCode.A), SectionCode.SPEC.apply("A"));
  }

  @Test
  void testEmptyOnInvalidCode() {
    assertEquals(Optional.empty(), SectionCode.SPEC.apply("Q"));
  }

  @Test
  void testRangeParsingMatchesStringParsing() {
    assertAll(
        () -> assertRangeEquals("A"),
        () -> assertRangeEquals("D"),
        () -> assertRangeEquals("E"),
        () -> assertRangeEquals("H"),
        () -> assertRangeEquals("T"),
        () -> assertRangeEquals("R"),
        () -> assertRangeEquals("P"),
        () -> assertRangeEquals("U"),
        () -> assertRangeEquals("Q"),
        () -> assertRangeEquals(""),
        () -> assertRangeEquals("AD"),
        () -> assertThrows(NullPointerException.class, () -> SectionCode.SPEC.apply(null)),
        () -> assertThrows(IndexOutOfBoundsException.class, () -> SectionCode.SPEC.parse("A", -1, 1)),
        () -> assertThrows(IndexOutOfBoundsException.class, () -> SectionCode.SPEC.parse("A", 0, 2)),
        () -> assertThrows(IndexOutOfBoundsException.class, () -> SectionCode.SPEC.parse("A", 1, 0)),
        () -> assertThrows(NullPointerException.class, () -> SectionCode.SPEC.parse(null, 0, 0))
    );
  }

  private static void assertRangeEquals(String fieldValue) {
    String source = "prefix" + fieldValue + "suffix";
    int startOffset = "prefix".length();
    assertEquals(
        SectionCode.SPEC.apply(fieldValue),
        SectionCode.SPEC.parse(source, startOffset, startOffset + fieldValue.length()));
  }
}
