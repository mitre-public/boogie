package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class TestCenterFix {

  @Test
  public void testFiltersTrimmableInput() {
    assertTrue(new CenterFix().filterInput("   "));
  }
}
