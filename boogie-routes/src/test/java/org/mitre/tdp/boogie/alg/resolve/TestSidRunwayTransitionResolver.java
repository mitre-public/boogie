package org.mitre.tdp.boogie.alg.resolve;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TransitionType;

class TestSidRunwayTransitionResolver {

  @Test
  void testNonDepartureRunwayTransitionFilter() {
    SidRunwayTransitionResolver resolver = new SidRunwayTransitionResolver("RW09", procedureId -> Collections.emptyList());

    Predicate<Transition> transitionPredicate = resolver.nonDepartureRunwayTransitionFilter();

    assertAll(
        "Collection of assertions against the runway transition resolution.",
        () -> assertFalse(transitionPredicate.test(newTransition("KARL", TransitionType.RUNWAY)), "Predicate should return false on mismatched transition type."),
        () -> assertTrue(transitionPredicate.test(newTransition("RW09B", TransitionType.RUNWAY)), "Predicate should return true on matching transition type and identifier."),
        () -> assertTrue(transitionPredicate.test(newTransition("ALL", TransitionType.COMMON)), "Predicate should return true for non-runway transitions.")
    );
  }

  private Transition newTransition(String transitionName, TransitionType transitionType) {
    Transition transition = mock(Transition.class);
    when(transition.transitionIdentifier()).thenReturn(Optional.of(transitionName));
    when(transition.transitionType()).thenReturn(transitionType);
    when(transition.toString()).thenReturn(transitionName.concat(":").concat(transitionType.name()));
    return transition;
  }
}
