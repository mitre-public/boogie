package org.mitre.tdp.boogie.alg.resolve;

import java.util.function.Predicate;

import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.alg.RunwayNumberExtractor;

/**
 * Configurable runway transition filter for use in down-selecting the set of available runway transitions for the flight to just
 * those servicing the arrival/departure runway.
 */
public class RunwayTransitionFilter implements Predicate<Transition> {

  private final String runwayId;

  public RunwayTransitionFilter(String runwayId) {
    this.runwayId = runwayId;
  }

  @Override
  public boolean test(Transition transition) {
    return transition.transitionIdentifier().filter(id -> id.contains(runwayId) || runwayId.contains(id)).isPresent()
        // SID/STAR transitions serving multiple runways off the same end of the airport typically are tagged with RWY01B
        // as the name for a transition servicing RW01R/L
        || transition.transitionIdentifier().filter(id -> id.endsWith("B")).flatMap(id -> RunwayNumberExtractor.INSTANCE.runwayNumber(runwayId).filter(id::contains)).isPresent();
  }
}
