package org.mitre.tdp.boogie.v18.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.v18.spec.common.AltitudeFlightLevel;

public class TestAltitudeFlightLevel {

  @Test
  public void testFiltersEmptyInputs() {
    AltitudeFlightLevel parser = mock(AltitudeFlightLevel.class);
    when(parser.filterInput(anyString())).thenCallRealMethod();
    assertTrue(parser.filterInput("   "));
  }

  @Test
  public void testParseValidAltitude() {
    AltitudeFlightLevel parser = mock(AltitudeFlightLevel.class);
    when(parser.parseValue(anyString())).thenCallRealMethod();
    assertEquals(18000.0, parser.parseValue("18000"));
  }

  @Test
  public void testParseValidFlightLevel() {
    AltitudeFlightLevel parser = mock(AltitudeFlightLevel.class);
    when(parser.parseValue(anyString())).thenCallRealMethod();
    assertEquals(18000.0, parser.parseValue("FL180"));
  }
}
