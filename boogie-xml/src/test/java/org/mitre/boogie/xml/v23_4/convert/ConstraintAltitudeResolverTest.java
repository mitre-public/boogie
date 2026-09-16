package org.mitre.boogie.xml.v23_4.convert;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mitre.boogie.xml.v23_4.generated.Constraint;
import org.mitre.boogie.xml.v23_4.generated.RouteMinimumAltitude;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ConstraintAltitudeResolverTest {

  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  void resolvesConstraintAltitudeInFeetForEitherPressureReference(boolean flightLevel) {
    Constraint altitude = new Constraint();
    // v23_4/schemas/Types/DataTypes.xsd: AltitudeValue explicitly encodes FL270 as 27000.
    altitude.setAltitude(27000);
    altitude.setIsFlightLevel(flightLevel);

    assertEquals(27000, ConstraintAltitudeResolver.resolve(altitude));
  }

  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  void resolvesAirspaceRouteAndHoldAltitudeInFeetForEitherPressureReference(boolean flightLevel) {
    RouteMinimumAltitude altitude = new RouteMinimumAltitude();
    altitude.setAltitude(27000);
    altitude.setIsFlightLevel(flightLevel);

    assertEquals(27000, ConstraintAltitudeResolver.resolve(altitude));
  }

  @Test
  void leavesUnavailableAirspaceRouteAndHoldAltitudesAbsent() {
    RouteMinimumAltitude altitude = new RouteMinimumAltitude();
    assertNull(ConstraintAltitudeResolver.resolve(altitude));

    altitude.setAltitude(27000);
    altitude.setIsNotam(true);
    assertNull(ConstraintAltitudeResolver.resolve(altitude));
  }
}
