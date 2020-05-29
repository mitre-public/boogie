package org.mitre.tdp.boogie.v18.field;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.v18.spec.field.IcaoRegion;

public class TestIcaoRegion {

  @Test
  public void testFiltersEmptyInput() {
    assertTrue(new IcaoRegion().filterInput("   "));
  }
}
