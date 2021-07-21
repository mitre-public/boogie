package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class TestRnp {

  private static final Rnp parser = new Rnp();

  @Test
  void testParseValidRnp100() {
    assertEquals(Optional.of(10.), parser.apply("100"));
  }

  @Test
  void testParseValidRnp013() {
    assertEquals(0.001, parser.apply("013").orElseThrow(AssertionError::new), 0.00001);
  }

  @Test
  void testParseValidRnp010() {
    assertEquals(1., parser.apply("010").orElseThrow(AssertionError::new), 0.00001);
  }

  @Test
  void testParseValidRnp302() {
    assertEquals(0.3, parser.apply("302").orElseThrow(AssertionError::new), 0.00001);
  }

  @Test
  void testParseValidRnp031() {
    assertEquals(0.3, parser.apply("031").orElseThrow(AssertionError::new), 0.00001);
  }

  @Test
  void testParseValidRnp990() {
    assertEquals(99.0, parser.apply("990").orElseThrow(AssertionError::new), 0.00001);
  }

  @Test
  void testFiltersInvalidInputs() {
    assertEquals(Optional.empty(), parser.apply("A01"));
  }
}
