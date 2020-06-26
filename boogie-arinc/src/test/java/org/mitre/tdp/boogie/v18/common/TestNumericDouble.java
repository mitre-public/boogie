package org.mitre.tdp.boogie.v18.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.FieldSpecParseException;
import org.mitre.tdp.boogie.v18.spec.common.NumericDouble;

public class TestNumericDouble {

  @Test
  public void testFiltersEmptyStringInputs() {
    NumericDouble parser = mock(NumericDouble.class);
    when(parser.filterInput(anyString())).thenCallRealMethod();
    assertTrue(parser.filterInput("   "));
  }

  @Test
  public void testAllowsLeadingSpaceIfRestNumeric() {
    NumericDouble parser = mock(NumericDouble.class);
    when(parser.filterInput(anyString())).thenCallRealMethod();
    assertFalse(parser.filterInput(" 300"));
  }

  @Test
  public void testParseValidSpaceLeadNumber() {
    NumericDouble parser = mock(NumericDouble.class);
    when(parser.validValue(anyString())).thenCallRealMethod();
    when(parser.parseValue(anyString())).thenCallRealMethod();
    assertEquals(10.0d, parser.parseValue(" 010"));
  }

  @Test
  public void testParseValidFloat() {
    NumericDouble parser = mock(NumericDouble.class);
    when(parser.validValue(anyString())).thenCallRealMethod();
    when(parser.parseValue(anyString())).thenCallRealMethod();
    assertEquals(10.0d, parser.parseValue("010"));
  }

  @Test
  public void testParseValidPositiveFloat() {
    NumericDouble parser = mock(NumericDouble.class);
    when(parser.validValue(anyString())).thenCallRealMethod();
    when(parser.parseValue(anyString())).thenCallRealMethod();
    assertEquals(10.0d, parser.parseValue("+010"));
  }

  @Test
  public void testParseValidNegativeFloat() {
    NumericDouble parser = mock(NumericDouble.class);
    when(parser.validValue(anyString())).thenCallRealMethod();
    when(parser.parseValue(anyString())).thenCallRealMethod();
    assertEquals(-10.0d, parser.parseValue("-010"));
  }

  @Test
  public void testParseExceptionInvalidFloat() {
    NumericDouble parser = mock(NumericDouble.class);
    when(parser.validValue(anyString())).thenCallRealMethod();
    when(parser.parseValue(anyString())).thenCallRealMethod();
    assertThrows(FieldSpecParseException.class, () -> parser.parseValue("01A"));
  }

  @Test
  public void testParseExceptionInvalidNegativeFloat() {
    NumericDouble parser = mock(NumericDouble.class);
    when(parser.validValue(anyString())).thenCallRealMethod();
    when(parser.parseValue(anyString())).thenCallRealMethod();
    assertThrows(FieldSpecParseException.class, () -> parser.parseValue("-01A"));
  }
}
