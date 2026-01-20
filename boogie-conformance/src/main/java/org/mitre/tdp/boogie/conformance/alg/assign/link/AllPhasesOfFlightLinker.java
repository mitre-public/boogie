package org.mitre.tdp.boogie.conformance.alg.assign.link;

import java.util.Collection;
import java.util.List;

import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.conformance.alg.assign.Route;

public class AllPhasesOfFlightLinker implements LinkingStrategy {
  private final Collection<Route> departureAirport;
  private final Collection<Route> sids;
  private final Collection<Route> departureTransition;
  private final Collection<Route> enroutes;
  private final Collection<Route> terminalArea1;
  private final Collection<Route> stars;
  private final Collection<Route> terminalArea2;
  private final Collection<Route> approaches;
  private final Collection<Route> arrivalAirport;

  @Override
  public Collection<Pair<FlyableLeg, FlyableLeg>> links(Collection<FlyableLeg> flyableLegs) {
    return List.of();
  }
}
