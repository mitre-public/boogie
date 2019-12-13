package org.mitre.tdp.boogie.alg.enroute;

import java.util.List;

import org.mitre.caasd.commons.TimeWindow;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.LegType;

/**
 * The enroute interface for modeling the predicted behaviour of an aircraft as it traverses a route.
 *
 * The primary concern of the enroute modeler is to understand how the aircraft gets from the end
 * of the previous {@link Leg} through the next {@link Leg} and the key derived feature it's looking
 * to extract from that behaviour is the time it will take to traverse that leg.
 */
public interface EnrouteModel {

  /**
   * Entry method into any enroute modeler.
   *
   * This method takes the previous {@link LegType#concrete()} and the next concrete leg as well as
   * any non-concrete legs in between and models the time it takes to get from the previous to the next.
   */
  TimeWindow predict(Leg previousConcrete, Leg nextConcrete, List<Leg> between);
}
