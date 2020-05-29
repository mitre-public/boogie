package org.mitre.tdp.boogie.v18.field;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.v18.spec.field.FixIdentifier;

public class TestFixIdentifier {

  @Test
  public void testFiltersTrimmableInput() {
    assertTrue(new FixIdentifier().filterInput("   "));
  }
}
