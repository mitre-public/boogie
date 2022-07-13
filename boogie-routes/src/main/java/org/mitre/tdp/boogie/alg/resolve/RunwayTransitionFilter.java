package org.mitre.tdp.boogie.alg.resolve;

import java.util.function.Predicate;

import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.alg.RunwayIdExtractor;

/**
 * Configurable runway transition filter for use in down-selecting the set of available runway transitions for the flight to just
 * those servicing the arrival/departure runway.
 */
final class RunwayTransitionFilter implements Predicate<Transition> {

  private final String runwayId;

  public RunwayTransitionFilter(String runwayId) {
    this.runwayId = runwayId;
  }

  @Override
  public boolean test(Transition transition) {
    return transition.transitionIdentifier()
        .map(this::doesTheTransitionMatch)
        .orElse(false);
  }

  private boolean doesTheTransitionMatch(String transitionIdentifier) {
    return transitionIdentifier.contains(runwayId) || runwayId.contains(transitionIdentifier) || transitionNameWithBMatches(transitionIdentifier);
  }

  private boolean transitionNameWithBMatches(String transitionIdentifier) {
    return RunwayIdExtractor.runwayNumber(runwayId)
        .map(number -> transitionIdentifier.endsWith("B") && transitionIdentifier.contains(number))
        .orElse(false);
  }
}
