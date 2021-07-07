package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TestTheta {

  @Test
  public void testParseValidTheta() {
    assertEquals(56.7, new Theta().parseValue(" 567"));
  }
}
