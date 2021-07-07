package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

public class TestBooleanString {

  @Test
  public void testFiltersTrimmableInput() {
    BooleanString spec = mock(BooleanString.class);
    when(spec.filterInput(anyString())).thenCallRealMethod();
    assertTrue(spec.filterInput("   "));
  }

  @Test
  public void testTrueValuesReturnTrue() {
    BooleanString spec = mock(BooleanString.class);
    when(spec.trueValues()).thenCallRealMethod();
    when(spec.parseValue(anyString())).thenCallRealMethod();

    spec.trueValues().forEach(value -> assertEquals(true, spec.parseValue(value), "True value not parsed to true: " + value));
  }

  @Test
  public void testNonTrueValuesReturnFalse() {
    BooleanString spec = mock(BooleanString.class);
    when(spec.trueValues()).thenCallRealMethod();
    when(spec.parseValue(anyString())).thenCallRealMethod();

    assertEquals(false, spec.parseValue("HI"));
  }
}
