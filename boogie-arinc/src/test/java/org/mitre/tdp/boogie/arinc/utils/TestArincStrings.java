package org.mitre.tdp.boogie.arinc.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mitre.tdp.boogie.arinc.utils.ArincStrings.copyWithBlanks;
import static org.mitre.tdp.boogie.arinc.utils.ArincStrings.isBlank;
import static org.mitre.tdp.boogie.arinc.utils.ArincStrings.parseDoubleWithTenths;
import static org.mitre.tdp.boogie.arinc.utils.ArincStrings.parseDoubleWithThousandths;
import static org.mitre.tdp.boogie.arinc.utils.ArincStrings.toEnumValue;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.FieldSpec;
import org.mitre.tdp.boogie.arinc.FieldSpecParseException;

class TestArincStrings {

  @Test
  void testEmptyStringIsBlank() {
    assertTrue(isBlank.test(""));
  }

  @Test
  void testBlankStringIsBlank() {
    assertTrue(isBlank.test("     "));
  }

  @Test
  void testCopyWithBlanks() {
    assertEquals("    ", copyWithBlanks("1234"));
  }

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

  enum Dummy implements FieldSpec<Dummy> {
    A;

    @Override
    public int fieldLength() {
      return 0;
    }

    @Override
    public String fieldCode() {
      return "HELP";
    }

    @Override
    public Dummy parseValue(String fieldValue) {
      return toEnumValue(fieldValue, Dummy.class);
    }
  }

  @Test
  void testToEnumValueWithValidString() {
    assertEquals(Dummy.A, toEnumValue("A", Dummy.class));
  }

  @Test
  void testToEnumValueThrowsParseExceptionWhenNameNotFound() {
    assertThrows(FieldSpecParseException.class, () -> toEnumValue("C", Dummy.class));
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
