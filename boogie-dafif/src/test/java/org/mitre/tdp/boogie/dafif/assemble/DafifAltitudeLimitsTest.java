package org.mitre.tdp.boogie.dafif.assemble;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mitre.tdp.boogie.AltitudeLimit;
import org.mitre.tdp.boogie.AltitudeLimits;

class DafifAltitudeLimitsTest {

  @Test
  void readsEachLimitInItsOwnReference() {
    assertAll(
        () -> assertEquals(AltitudeLimits.of(AltitudeLimit.msl(195.0), AltitudeLimit.flightLevel(180)), DafifAltitudeLimits.INSTANCE.apply("00195AMSL", "FL180")),
        () -> assertEquals(AltitudeLimits.of(AltitudeLimit.flightLevel(240), AltitudeLimit.none()), DafifAltitudeLimits.INSTANCE.apply("FL240", "UNLTD")),
        () -> assertEquals(AltitudeLimits.of(AltitudeLimit.agl(500.0), AltitudeLimit.flightLevel(180)), DafifAltitudeLimits.INSTANCE.apply("00500AGL", "FL180")),
        () -> assertEquals(AltitudeLimits.of(AltitudeLimit.ground(), AltitudeLimit.agl(1_200.0)), DafifAltitudeLimits.INSTANCE.apply("SURFACE", "001200AGL")),
        () -> assertEquals(AltitudeLimits.of(AltitudeLimit.ground(), AltitudeLimit.msl(2_500.0)), DafifAltitudeLimits.INSTANCE.apply("GND", "002500AMSL")));
  }

  @ParameterizedTest
  @ValueSource(strings = {"U", "BY NOTAM", " "})
  void limitsTheSourceDoesNotPlaceAreNotKnown(String lower) {
    assertEquals(AltitudeLimits.of(lower.equals("BY NOTAM") ? AltitudeLimit.byNotam() : AltitudeLimit.notSpecified(), AltitudeLimit.flightLevel(180)),
        DafifAltitudeLimits.INSTANCE.apply(lower, "FL180"));
  }

  @Test
  void contradictoryLimitsAreNotKnownAndUnreadableOnesAreRejected() {
    assertAll(
        () -> assertEquals(AltitudeLimits.unknown(), DafifAltitudeLimits.INSTANCE.apply("FL200", "FL180")),
        () -> assertThrows(IllegalArgumentException.class, () -> DafifAltitudeLimits.INSTANCE.apply("SOMETIMES", "FL180")));
  }
}
