package org.mitre.tdp.boogie.v18.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.v18.spec.common.AltitudeFlightLevel;

public class TestAltitudeFlightLevel {

  @Test
  public void testParseValidAltitude() {
    AltitudeFlightLevel parser = mock(AltitudeFlightLevel.class);
    when(parser.parse(anyString())).thenCallRealMethod();
    assertEquals(18000.0f, parser.parse("18000"));
  }

  @Test
  public void testParseValidFlightLevel() {
    AltitudeFlightLevel parser = mock(AltitudeFlightLevel.class);
    when(parser.parse(anyString())).thenCallRealMethod();
    assertEquals(18000.0f, parser.parse("FL180"));
  }
}
