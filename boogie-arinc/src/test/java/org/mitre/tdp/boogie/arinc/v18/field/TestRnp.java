package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.FieldSpecParseException;
import org.mitre.tdp.boogie.arinc.v18.field.Rnp;

public class TestRnp {

  @Test
  public void testParseValidRnp100() {
    assertEquals(10.0f, new Rnp().parseValue("100"));
  }

  @Test
  public void testParseValidRnp013() {
    assertEquals(0.001, new Rnp().parseValue("013"), 0.00001);
  }

  @Test
  public void testParseValidRnp010() {
    assertEquals(1., new Rnp().parseValue("010"), 0.00001);
  }

  @Test
  public void testParseValidRnp302() {
    assertEquals(0.3, new Rnp().parseValue("302"), 0.00001);
  }

  @Test
  public void testParseValidRnp031() {
    assertEquals(0.3, new Rnp().parseValue("031"), 0.00001);
  }

  @Test
  public void testParseValidRnp990() {
    assertEquals(99.0, new Rnp().parseValue("990"), 0.00001);
  }

  @Test
  public void testParseExceptionInvalidValue() {
    assertThrows(FieldSpecParseException.class, () -> new Rnp().parseValue("A01"));
  }
}
