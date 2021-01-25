package org.mitre.tdp.boogie.alg.resolve;

import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.alg.resolve.element.ProcedureElement;

/**
 * Filter to be applied to the internal transitions of {@link ProcedureElement}s to down-select the set of candidate
 * transitions to consider when resolving the initial route of the flight.
 *
 * This is essentially just down selecting the transitions based on whether the runway transition name contains the
 * provided runway identifier.
 */
public final class SidRunwayTransitionFilter extends RunwayTransitionFilter {

  public SidRunwayTransitionFilter(String runwayId) {
    super(runwayId);
  }

  @Override
  public boolean test(Transition transition) {
    return transition.transitionType().equals(TransitionType.RUNWAY)
        && transition.procedureType().equals(ProcedureType.SID)
        && super.test(transition);
  }
}
