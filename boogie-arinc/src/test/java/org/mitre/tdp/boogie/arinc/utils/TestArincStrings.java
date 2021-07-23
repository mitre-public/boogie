package org.mitre.tdp.boogie.arinc.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mitre.tdp.boogie.arinc.utils.ArincStrings.parseDoubleWithTenths;
import static org.mitre.tdp.boogie.arinc.utils.ArincStrings.parseDoubleWithThousandths;

import org.junit.jupiter.api.Test;

class TestArincStrings {

  @Test
  void testParseDoubleWithTenths() {
    assertEquals(15.7, parseDoubleWithTenths("157"));
  }

  @Test
  void testParseDoubleWithTenthsLonger() {
    assertEquals(1234567.8, parseDoubleWithTenths("12345678"));
  }

  @Test
  void testParseDoubleWithTenthsShorter() {
    assertEquals(0.5, parseDoubleWithTenths("5"));
  }

  @Test
  void testParseNegativeDoubleWithTenths() {
    assertEquals(-0.3, parseDoubleWithTenths("-03"));
  }

  @Test
  void testParseNegativeDoubleWithThousandths() {
    assertEquals(-3.05, parseDoubleWithThousandths("-3050"));
  }
}
