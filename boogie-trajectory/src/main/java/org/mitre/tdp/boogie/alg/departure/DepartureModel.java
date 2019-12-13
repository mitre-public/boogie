package org.mitre.tdp.boogie.alg.departure;

import org.mitre.caasd.commons.TimeWindow;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Leg;

/**
 * Base interface for modeling a departure from a given airport.
 */
public interface DepartureModel {

  /**
   * Predicts the time between when an aircraft departs an airport and when it joins the primary route.
   */
  TimeWindow predict(Airport departure, Leg initial);
}
