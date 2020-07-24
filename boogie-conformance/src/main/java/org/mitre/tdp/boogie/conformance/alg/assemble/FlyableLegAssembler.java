package org.mitre.tdp.boogie.conformance.alg.assemble;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.utils.Iterators;

public interface FlyableLegAssembler {

  /**
   * Generates a collection of leg triples from the input list of legs - optionally in a bidirectional
   * fashion.
   */
  default List<FlyableLeg> assemble(List<? extends Leg> legs) {
    if (legs.isEmpty()) {
      return Collections.emptyList();
    }

    List<FlyableLeg> forward = new ArrayList<>();
    if (legs.size() == 1) {
      forward.add(new FlyableLeg(null, legs.get(0), null));
    } else if (legs.size() == 2) {
      forward.add(new FlyableLeg(legs.get(0), legs.get(1), null));
      forward.add(new FlyableLeg(null, legs.get(0), legs.get(1)));
    } else {
      forward.add(new FlyableLeg(null, legs.get(0), legs.get(1)));
      Iterators.triples(legs, (l1, l2, l3) -> forward.add(new FlyableLeg(l1, l2, l3)));
      forward.add(new FlyableLeg(legs.get(legs.size() - 2), legs.get(legs.size() - 1), null));
    }

    return forward;
  }

  /**
   * Assembles a list of sequential, bidirectional, {@link FlyableLeg}s from the given {@link Airway}.
   */
  default List<FlyableLeg> assembleFrom(Airway airway) {
    return assemble(airway.legs());
  }

  /**
   * Assembles a list of sequential {@link FlyableLeg}s from the given {@link Transition}.
   */
  default List<FlyableLeg> assembleFrom(Transition transition) {
    return assemble(transition.legs());
  }

  static FlyableLegAssembler allTriples() {
    return new FlyableLegAssembler() {};
  }
}
