package org.mitre.tdp.boogie.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.v18.spec.field.RouteHoldDistanceTime;

public class TestRouteHoldDistanceTime {

  @Test
  public void testRouteHoldDistance() {
    assertEquals(107.6, new RouteHoldDistanceTime().asDistanceInNm("1076").get());
  }

  @Test
  public void testParseExceptionOnTimeAsDistance() {
    assertFalse(new RouteHoldDistanceTime().asDistanceInNm("T103").isPresent());
  }

  @Test
  public void testRouteHoldTime() {
    assertEquals(Duration.ofMinutes(10), new RouteHoldDistanceTime().asDuration("T100").get());
  }

  @Test
  public void testParseExceptionOnDistanceAsTime() {
    assertFalse(new RouteHoldDistanceTime().asDuration("1076").isPresent());
  }
}
