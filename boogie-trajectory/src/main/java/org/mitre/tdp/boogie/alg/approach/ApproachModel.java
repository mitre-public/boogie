package org.mitre.tdp.boogie.alg.approach;

import org.mitre.caasd.commons.TimeWindow;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Leg;

/**
 * The base interface for modeling an approach.
 */
public interface ApproachModel {

  /**
   * Predicts the time it takes to get from the terminal {@link Leg} onto the surface at the airport.
   */
  TimeWindow predict(Leg terminal, Airport arrival);
}
