package org.mitre.boogie.xml.v23_4.convert;

import org.mitre.boogie.xml.v23_4.generated.AirspaceRouteHoldAltitude;
import org.mitre.boogie.xml.v23_4.generated.Constraint;

final class ConstraintAltitudeResolver {

  private ConstraintAltitudeResolver() {
  }

  static int resolve(Constraint constraint) {
    return Boolean.TRUE.equals(constraint.isIsFlightLevel()) ? constraint.getAltitude() * 100 : constraint.getAltitude();
  }

  static Integer resolve(AirspaceRouteHoldAltitude altitude) {
    if (Boolean.TRUE.equals(altitude.isIsNotSpecified()) || Boolean.TRUE.equals(altitude.isIsUnknown()) || Boolean.TRUE.equals(altitude.isIsNotam())) {
      return null;
    }
    Integer value = altitude.getAltitude();
    if (value == null) {
      return null;
    }
    return Boolean.TRUE.equals(altitude.isIsFlightLevel()) ? value * 100 : value;
  }
}
