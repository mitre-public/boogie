package org.mitre.tdp.boogie.v18.field;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.v18.spec.field.RegionCode;

public class TestRegionCode {

  @Test
  public void testFiltersTrimmableInput() {
    assertTrue(new RegionCode().filterInput("   "));
  }
}
