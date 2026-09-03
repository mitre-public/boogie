package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Duration;
import java.util.Optional;

import org.junit.jupiter.api.Test;

class TestRouteHoldDistanceTime {

  private static final RouteHoldDistanceTime parser = new RouteHoldDistanceTime();

  @Test
  void testRouteHoldDistance() {
    assertAll(
        () -> assertEquals(Optional.of(107.6), parser.asDistanceInNm("1076")),
        () -> assertEquals(Optional.empty(), parser.asDistanceInNm("10A6")),
        () -> assertEquals(Optional.empty(), parser.asDistanceInNm("-076")),
        () -> assertThrows(NullPointerException.class, () -> parser.asDistanceInNm(null))
    );
  }

  @Test
  void testParseExceptionOnTimeAsDistance() {
    assertEquals(Optional.empty(), parser.asDistanceInNm("T103"));
  }

  @Test
  void testRouteHoldTime() {
    assertAll(
        () -> assertEquals(Optional.of(Duration.ofMinutes(10)), parser.asDuration("T100")),
        () -> assertEquals(Optional.of(Duration.ofSeconds(618)), parser.asDuration("T103")),
        () -> assertEquals(Optional.empty(), parser.asDuration("T10A")),
        () -> assertEquals(Optional.empty(), parser.asDuration("T")),
        () -> assertThrows(NullPointerException.class, () -> parser.asDuration(null))
    );
  }

  @Test
  void testParseExceptionOnDistanceAsTime() {
    assertEquals(Optional.empty(), parser.asDuration("1076"));
  }
}
