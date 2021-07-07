package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class TestVorNdbIdentifier {

  private static final VorNdbIdentifier vorNdbIdentifier = new VorNdbIdentifier();

  @Test
  void testFiltersTrimmableInput() {
    assertTrue(vorNdbIdentifier.filterInput("  "));
  }

  @Test
  void testReturnsEmptyOnEmptyInput() {
    assertEquals(Optional.empty(), vorNdbIdentifier.parse("   "));
  }
}
