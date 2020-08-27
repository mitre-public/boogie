package org.mitre.tdp.boogie.alg.resolve;

import java.util.function.Predicate;

import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TransitionType;

/**
 * Filter function only allowing through common or enroute transitions.
 */
public class CommonOrEnrouteTransitionFilter implements Predicate<Transition> {
  @Override
  public boolean test(Transition transition) {
    return transition.transitionType().equals(TransitionType.COMMON) || transition.transitionType().equals(TransitionType.ENROUTE);
  }
}
