package org.mitre.tdp.boogie;

import org.mitre.caasd.commons.Course;
import org.mitre.caasd.commons.Distance;
import org.mitre.caasd.commons.LatLong;

public interface Runway {

  /**
   * The identifier of the runway.
   *
   * <p>Generally runway names are based on the magnetic
   */
  String runwayIdentifier();

  LatLong origin();

  Distance length();

  Course course();
}
