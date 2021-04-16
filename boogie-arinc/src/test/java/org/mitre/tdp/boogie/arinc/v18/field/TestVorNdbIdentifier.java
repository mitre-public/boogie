package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.v18.field.VorNdbIdentifier;

public class TestVorNdbIdentifier {

  @Test
  public void testFiltersTrimmableInput() {
    assertTrue(new VorNdbIdentifier().filterInput("  "));
  }
}
