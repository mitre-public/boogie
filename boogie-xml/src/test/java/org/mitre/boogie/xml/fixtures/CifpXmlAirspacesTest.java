package org.mitre.boogie.xml.fixtures;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.v23_4.generated.AirspaceAltitudeConstraint;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CifpXmlAirspacesTest {

  @Test
  void preservesGroundAndUnlimitedLimitsLostByTheNumericModel() {
    AirspaceAltitudeConstraint limits = CifpXmlAirspaces.limits(Optional.empty(), Optional.empty(), "GND  ", "UNLTD");

    assertAll(
        () -> assertTrue(limits.getLowerLimit().isIsGround()),
        () -> assertTrue(limits.getUpperLimit().isIsUnlimited()),
        () -> assertNull(limits.getLowerLimit().getAltitude()),
        () -> assertNull(limits.getUpperLimit().getAltitude())
    );
  }

  @Test
  void preservesFlightLevelReferenceWithTheSchemaAltitudeInFeet() {
    AirspaceAltitudeConstraint limits = CifpXmlAirspaces.limits(Optional.of(18000.0), Optional.of(45000.0), "FL180", "FL450");

    assertAll(
        () -> assertEquals(18000, limits.getLowerLimit().getAltitude()),
        () -> assertEquals(45000, limits.getUpperLimit().getAltitude()),
        () -> assertTrue(limits.getLowerLimit().isIsFlightLevel()),
        () -> assertTrue(limits.getUpperLimit().isIsFlightLevel())
    );
  }

  @Test
  void leavesNumericAltitudesInFeet() {
    AirspaceAltitudeConstraint limits = CifpXmlAirspaces.limits(Optional.of(500.0), Optional.of(17999.0), "00500", "17999");

    assertAll(
        () -> assertEquals(500, limits.getLowerLimit().getAltitude()),
        () -> assertEquals(17999, limits.getUpperLimit().getAltitude()),
        () -> assertNull(limits.getLowerLimit().isIsFlightLevel()),
        () -> assertNull(limits.getUpperLimit().isIsFlightLevel())
    );
  }

  @Test
  void leavesAbsentLimitsUnspecified() {
    assertNull(CifpXmlAirspaces.limits(Optional.empty(), Optional.empty(), "     ", "     "));
  }
}
