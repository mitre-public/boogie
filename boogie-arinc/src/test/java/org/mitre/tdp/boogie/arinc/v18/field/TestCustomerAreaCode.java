package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class TestCustomerAreaCode {

  @Test
  void testParserFiltersEmptyInputs() {
    assertEquals(Optional.empty(), CustomerAreaCode.SPEC.apply(""));
  }

  @Test
  void testParserFiltersWhitespaceInputs() {
    assertEquals(Optional.empty(), CustomerAreaCode.SPEC.apply("   "));
  }

  @Test
  void testParserReturnsValidBoundaryCodeOnMatch() {
    assertEquals(Optional.of(CustomerAreaCode.USA), CustomerAreaCode.SPEC.apply(" USA "));
  }
}
