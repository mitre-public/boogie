package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.FieldSpecParseException;

public class TestNameFormat {

  @Test
  public void testParserFiltersEmptyString() {
    assertTrue(new NameFormat().filterInput("   "));
  }

  @Test
  public void testParseValidNameFormatColumn1() {
    assertEquals("A  ", new NameFormat().parseValue("A  "));
  }

  @Test
  public void testParseValidNameFormatColumn2() {
    assertEquals(" O ", new NameFormat().parseValue(" O "));
  }

  @Test
  public void testParseValidNameFormatColumn3() {
    assertEquals("   ", new NameFormat().parseValue("   "));
  }

  @Test
  public void testParseInvalidNameFormatColumn1(){
    assertThrows(FieldSpecParseException.class, () -> new NameFormat().parseValue("Z  "));
  }

  @Test
  public void testParseInvalidNameFormatColumn2(){
    assertThrows(FieldSpecParseException.class, () -> new NameFormat().parseValue(" Q "));
  }

  @Test
  public void testParseInvalidNameFormatColumn3(){
    assertThrows(FieldSpecParseException.class, () -> new NameFormat().parseValue("  Z"));
  }
}
