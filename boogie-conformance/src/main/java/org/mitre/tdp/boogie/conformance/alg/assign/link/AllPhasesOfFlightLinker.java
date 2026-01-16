package org.mitre.tdp.boogie.conformance.alg.assign.link;

import java.util.Collection;
import java.util.List;

import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.conformance.alg.assign.Route;

public class AllPhasesOfFlightLinker implements LinkingStrategy {
  private Airport departure;
  private Collection<Route> sids;
  private Collection<?> departureTransition;
  private Collection<Route> enroutes;
  private Collection<?> terminalArea1;
  private Collection<Route> stars;
  private Collection<?> terminalArea2;
  private Collection<Route> approaches;
  private Airport airport;

  @Override
  public Collection<Pair<FlyableLeg, FlyableLeg>> links(Collection<FlyableLeg> flyableLegs) {
    return List.of();
  }
}
