package org.mitre.tdp.boogie;

import org.mitre.caasd.commons.LatLong;

public interface Runway extends Infrastructure {

  /**
   * The physical position of the base end of the runway.
   */
  LatLong baseEnd();

  /**
   * The physical position of the reciprocal end of the runway.
   */
  LatLong reciprocalEnd();
}
