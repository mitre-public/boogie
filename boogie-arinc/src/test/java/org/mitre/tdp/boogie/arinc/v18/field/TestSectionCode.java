package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class TestSectionCode {

  @Test
  void testValidSectionCode() {
    assertEquals(Optional.of(SectionCode.A), SectionCode.SPEC.apply("A"));
  }

  @Test
  void testEmptyOnInvalidCode() {
    assertEquals(Optional.empty(), SectionCode.SPEC.apply("Q"));
  }
}