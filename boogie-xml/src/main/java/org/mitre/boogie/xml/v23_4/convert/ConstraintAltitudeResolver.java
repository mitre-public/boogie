package org.mitre.boogie.xml.v23_4.convert;

import org.mitre.boogie.xml.v23_4.generated.AirspaceRouteHoldAltitude;
import org.mitre.boogie.xml.v23_4.generated.Constraint;

/**
 * Resolves the numeric altitude in feet. ARINC 424-23.4 Types/DataTypes.xsd defines AltitudeValue with the example
 * "FL270 is encoded as 27000"; isFlightLevel changes the pressure reference, not the numeric unit.
 */
final class ConstraintAltitudeResolver {

  private ConstraintAltitudeResolver() {
  }

  static int resolve(Constraint constraint) {
    return constraint.getAltitude();
  }

  static Integer resolve(AirspaceRouteHoldAltitude altitude) {
    if (Boolean.TRUE.equals(altitude.isIsNotSpecified()) || Boolean.TRUE.equals(altitude.isIsUnknown()) || Boolean.TRUE.equals(altitude.isIsNotam())) {
      return null;
    }
    return altitude.getAltitude();
  }
}
