package org.mitre.tdp.boogie.v18.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.FieldSpecParseException;
import org.mitre.tdp.boogie.v18.spec.common.NumericDouble;

public class TestNumericFloat {

  @Test
  public void testParseValidFloat() {
    NumericDouble parser = mock(NumericDouble.class);
    when(parser.parse(anyString())).thenCallRealMethod();
    assertEquals(10.0f, parser.parse("010"));
  }

  @Test
  public void testParseValidNegativeFloat() {
    NumericDouble parser = mock(NumericDouble.class);
    when(parser.parse(anyString())).thenCallRealMethod();
    assertEquals(-10.0f, parser.parse("-010"));
  }

  @Test
  public void testParseExceptionInvalidFloat() {
    NumericDouble parser = mock(NumericDouble.class);
    when(parser.parse(anyString())).thenCallRealMethod();
    assertThrows(FieldSpecParseException.class, () -> parser.parse("01A"));
  }

  @Test
  public void testParseExceptionInvalidNegativeFloat() {
    NumericDouble parser = mock(NumericDouble.class);
    when(parser.parse(anyString())).thenCallRealMethod();
    assertThrows(FieldSpecParseException.class, () -> parser.parse("-01A"));
  }
}
