package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.v18.field.IfrCapability;

public class TestIfrCapability {

  @Test
  public void testFiltersTrimmableInput() {
    assertTrue(new IfrCapability().filterInput("   "));
  }
}
