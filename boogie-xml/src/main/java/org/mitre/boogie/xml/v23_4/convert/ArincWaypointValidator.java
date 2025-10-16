package org.mitre.boogie.xml.v23_4.convert;

import static java.util.Objects.nonNull;

import java.io.Serializable;
import java.util.function.Predicate;

import org.mitre.boogie.xml.v23_4.generated.Waypoint;

public final class ArincWaypointValidator implements Predicate<Waypoint>, Serializable {
  public static final ArincWaypointValidator INSTANCE = new ArincWaypointValidator();
  private ArincWaypointValidator() {}
  @Override
  public boolean test(Waypoint waypoint) {
    return nonNull(waypoint.getRecordType())
        && nonNull(waypoint.getIdentifier())
        && nonNull(waypoint.getIcaoCode())
        && nonNull(waypoint.getLocation())
        && nonNull(waypoint.getLocation().getLatitude())
        && nonNull(waypoint.getLocation().getLongitude())
        && nonNull(waypoint.getCycleDate());
  }
}
