package org.mitre.tdp.boogie.alg.resolve;

import java.util.function.Predicate;

import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.alg.resolve.element.ProcedureElement;

/**
 * Filter to be applied to the internal transitions of {@link ProcedureElement}s to down-select the set of candidate
 * transitions to consider when resolving the final route of the flight.
 *
 * This is essentially just down selecting the transitions based on whether the runway transition name contains the
 * provided runway identifier.
 */
public class StarRunwayTransitionFilter implements Predicate<Transition> {

  private final String runwayId;

  public StarRunwayTransitionFilter(String runwayId) {
    this.runwayId = runwayId;
  }

  @Override
  public boolean test(Transition transition) {
    return !transition.transitionType().equals(TransitionType.RUNWAY)
        || !transition.procedureType().equals(ProcedureType.STAR)
        // checks that the transition ID contains the runway name - stripping RW on the off chance it was pre-pended
        || runwayId.contains(transition.identifier())
        || transition.identifier().contains(runwayId);
  }
}
