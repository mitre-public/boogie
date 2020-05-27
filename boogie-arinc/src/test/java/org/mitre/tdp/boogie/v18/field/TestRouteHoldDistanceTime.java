package org.mitre.tdp.boogie.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.FieldSpecParseException;
import org.mitre.tdp.boogie.v18.spec.field.RouteHoldDistanceTime;

public class TestRouteHoldDistanceTime {

  @Test
  public void testRouteHoldDistance() {
    assertEquals(107.6, new RouteHoldDistanceTime().asDistanceInNm("1076"));
  }

  @Test
  public void testParseExceptionOnTimeAsDistance() {
    assertThrows(FieldSpecParseException.class, () -> new RouteHoldDistanceTime().asDistanceInNm("T103"));
  }

  @Test
  public void testRouteHoldTime() {
    assertEquals(Duration.ofMinutes(10), new RouteHoldDistanceTime().asDuration("T100"));
  }

  @Test
  public void testParseExceptionOnDistanceAsTime() {
    assertThrows(FieldSpecParseException.class, () -> new RouteHoldDistanceTime().asDuration("1076"));
  }
}
