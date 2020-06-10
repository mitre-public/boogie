package org.mitre.tdp.boogie.v18.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.FieldSpecParseException;
import org.mitre.tdp.boogie.v18.spec.common.NumericInteger;

public class TestNumericInteger {

  @Test
  public void testFiltersEmptyStringInputs() {
    NumericInteger parser = mock(NumericInteger.class);
    when(parser.filterInput(anyString())).thenCallRealMethod();
    assertTrue(parser.filterInput("   "));
  }

  @Test
  public void testParseValidInteger() {
    NumericInteger parser = mock(NumericInteger.class);
    when(parser.parseValue(anyString())).thenCallRealMethod();
    assertEquals(1000, parser.parseValue("01000"));
  }

  @Test
  public void testParseValidNegativeInteger() {
    NumericInteger parser = mock(NumericInteger.class);
    when(parser.parseValue(anyString())).thenCallRealMethod();
    assertEquals(-1000, parser.parseValue("-1000"));
  }

  @Test
  public void testParseExceptionInvalidInteger() {
    NumericInteger parser = mock(NumericInteger.class);
    when(parser.parseValue(anyString())).thenCallRealMethod();
    assertThrows(FieldSpecParseException.class, () -> parser.parseValue("A1000"));
  }

  @Test
  public void testParseExceptionInvalidNegativeInteger() {
    NumericInteger parser = mock(NumericInteger.class);
    when(parser.parseValue(anyString())).thenCallRealMethod();
    assertThrows(FieldSpecParseException.class, () -> parser.parseValue("-A1000"));
  }
}
