package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class TestRegionCode {

  private static final RegionCode parser = new RegionCode();

  @Test
  void testFiltersTrimmableInput() {
    assertEquals(Optional.empty(), parser.apply("   "));
  }
}
