package org.mitre.tdp.boogie.arinc.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class TestArincDecimalParser {

  @Test
  void testParseDoubleWithTenths() {
    assertEquals(Optional.of(15.7), ArincDecimalParser.INSTANCE.parseDoubleWithTenths("157"));
  }

  @Test
  void testParseDoubleWithTenthsLonger() {
    assertEquals(Optional.of(1234567.8), ArincDecimalParser.INSTANCE.parseDoubleWithTenths("12345678"));
  }

  @Test
  void testParseDoubleWithTenthsShorter() {
    assertEquals(Optional.of(.5), ArincDecimalParser.INSTANCE.parseDoubleWithTenths("5"));
  }

  @Test
  void testParseNegativeDoubleWithTenths() {
    assertEquals(Optional.of(-.3), ArincDecimalParser.INSTANCE.parseDoubleWithTenths("-03"));
  }

  @Test
  void testParseNegativeDoubleWithThousandths() {
    assertEquals(Optional.of(-3.05), ArincDecimalParser.INSTANCE.parseDoubleWithThousandths("-3050"));
  }
}
