package org.mitre.tdp.boogie.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.v18.spec.field.Rho;

public class TestRho {

  @Test
  public void testParseValidRho() {
    assertEquals(30.5, new Rho().parseValue(" 305"));
  }
}
