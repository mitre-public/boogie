package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.v18.field.Rho;

public class TestRho {

  @Test
  public void testParseValidRho() {
    assertEquals(30.5, new Rho().parseValue(" 305"));
  }
}
