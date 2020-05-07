package org.mitre.tdp.boogie.conformance.alg.assemble;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.conformance.model.ConsecutiveLegs;
import org.mitre.tdp.boogie.conformance.model.SimpleConsecutiveLegs;
import org.mitre.tdp.boogie.utils.Iterators;

public class ConsecutiveLegFactory {

  /**
   * Builds a new collection of {@link ConsecutiveLegs} from an input ordered sequence of legs.
   */
  public static <L extends Leg> List<ConsecutiveLegs> fromLegSequence(List<L> legs) {
    if (legs.isEmpty()) {
      return Collections.emptyList();
    } else if (legs.size() == 1) {
      return Collections.singletonList(new SimpleConsecutiveLegs(null, legs.get(0), null));
    } else if (legs.size() == 2) {
      return Arrays.asList(
          new SimpleConsecutiveLegs(null, legs.get(0), legs.get(1)),
          new SimpleConsecutiveLegs(legs.get(0), legs.get(1), null));
    } else {
      List<ConsecutiveLegs> consecutiveLegs = new ArrayList<>();
      Iterators.triples(legs, (l1, l2, l3) -> consecutiveLegs.add(new SimpleConsecutiveLegs(l1, l2, l3)));
      return consecutiveLegs;
    }
  }

  public static <T extends Transition> List<ConsecutiveLegs> fromTransition(T transition) {
    return fromLegSequence(transition.legs());
  }

  public static <A extends Airway> List<ConsecutiveLegs> fromAirway(A airway) {
    return fromLegSequence(airway.legs());
  }
}
