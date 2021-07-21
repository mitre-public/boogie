package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import java.util.Optional;

import org.junit.jupiter.api.Test;

class TestRouteHoldDistanceTime {

  private static final RouteHoldDistanceTime parser = new RouteHoldDistanceTime();

  @Test
  void testRouteHoldDistance() {
    assertEquals(Optional.of(107.6), parser.asDistanceInNm("1076"));
  }

  @Test
  void testParseExceptionOnTimeAsDistance() {
    assertEquals(Optional.empty(), parser.asDistanceInNm("T103"));
  }

  @Test
  void testRouteHoldTime() {
    assertEquals(Optional.of(Duration.ofMinutes(10)), parser.asDuration("T100"));
  }

  @Test
  void testParseExceptionOnDistanceAsTime() {
    assertEquals(Optional.empty(), parser.asDuration("1076"));
  }
}
