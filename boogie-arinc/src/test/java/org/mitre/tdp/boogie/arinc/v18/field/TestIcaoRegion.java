package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class TestIcaoRegion {

  @Test
  public void testFiltersEmptyInput() {
    assertTrue(new IcaoRegion().filterInput("   "));
  }
}
